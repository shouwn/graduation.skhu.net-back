package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.ApiResponse
import com.shouwn.graduation.model.domain.dto.JwtAuthenticationResponse
import com.shouwn.graduation.model.domain.dto.LoginRequest
import com.shouwn.graduation.model.domain.dto.SignUpRequest
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.exception.AppException
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.repository.RoleRepository
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid

@Service
class AuthService @Autowired constructor(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val tokenProvider: JwtTokenProvider
) {
    private val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    fun authenticateUser(loginRequest: LoginRequest): JwtAuthenticationResponse {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.usernameOrEmail, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)

        return JwtAuthenticationResponse(jwt)
    }

    fun registerUser(signUpRequest: SignUpRequest): URI {
        if (userRepository.existsByUsername(signUpRequest.username))
            throw ApiException(
                    apiResponse = ApiResponse(false, "Username is already taken!"),
                    status = HttpStatus.BAD_REQUEST)

        if (userRepository.existsByEmail(signUpRequest.email))
            throw ApiException(
                    apiResponse = ApiResponse(false, "Email Address already in use!"),
                    status = HttpStatus.BAD_REQUEST)

        val user = User(
                name = signUpRequest.name,
                username = signUpRequest.username,
                email = signUpRequest.email,
                password = passwordEncoder.encode(signUpRequest.password),
                roles = mutableSetOf(roleRepository.findByRole(RoleName.ROLE_USER)
                        ?: throw AppException("User Role not Set."))
        )

        val result = userRepository.save(user)

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.username).toUri()
    }
}