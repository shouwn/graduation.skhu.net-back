package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.ApiResponse
import com.shouwn.graduation.model.domain.dto.JwtAuthenticationResponse
import com.shouwn.graduation.model.domain.dto.LoginRequest
import com.shouwn.graduation.model.domain.dto.SignUpRequest
import com.shouwn.graduation.model.domain.entity.User
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("api/auth")
class AuthController @Autowired constructor(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val tokenProvider: JwtTokenProvider
){

    @PostMapping("signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*>{
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.usernameOrEmail, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)

        return ResponseEntity.status(HttpStatus.CREATED).body(JwtAuthenticationResponse(jwt))
    }

    @PostMapping("signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*>{
        if(userRepository.existsByUsername(signUpRequest.username))
            return ResponseEntity(ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST)

        if(userRepository.existsByEmail(signUpRequest.email))
            return ResponseEntity(ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST)

        val user = User(
                name = signUpRequest.name,
                username = signUpRequest.username,
                email = signUpRequest.email,
                password = "{noop}${signUpRequest.password}", // 바꿔야함
                roles = mutableSetOf(roleRepository.findByRole(RoleName.ROLE_USER) ?: throw AppException("User Role not Set."))
        )

        val result = userRepository.save(user)

        val location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.username).toUri()

        return ResponseEntity.created(location).body(ApiResponse(true, "User registered successfully"))
    }
}