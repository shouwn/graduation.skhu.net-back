package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.response.JwtAuthenticationResponse
import com.shouwn.graduation.model.domain.dto.request.LoginRequest
import com.shouwn.graduation.model.domain.dto.request.SignUpRequest
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.exception.AppException
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.repository.RoleRepository
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Service
class AuthService @Autowired constructor(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val tokenProvider: JwtTokenProvider
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)
    private val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Value("\${app.signCode.user}")
    private lateinit var userSignCode: String

    @Value("\${app.signCode.admin}")
    private lateinit var adminSignCode: String

    fun authenticateUser(loginRequest: LoginRequest): JwtAuthenticationResponse {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.userNumberOrEmail, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)

        logger.info("${loginRequest.userNumberOrEmail} is login successful!")

        return JwtAuthenticationResponse(jwt)
    }

    fun registerUser(signUpRequest: SignUpRequest, role: RoleName): URI {
        val signCode = if(role == RoleName.ROLE_USER) userSignCode else adminSignCode

        if(signUpRequest.code != signCode) {
            logger.error("Register Fail: ${signUpRequest.code} is not $signCode")

            throw ApiException(
                    apiResponse = ApiResponse(false, "인증코드가 맞지 않습니다."),
                    status = HttpStatus.UNAUTHORIZED
            )
        }

        if (userRepository.existsByUserNumberOrEmail(signUpRequest.userNumber, signUpRequest.email)) {
            logger.error("Register Fail: ${signUpRequest.userNumber} or ${signUpRequest.email} is not already exist")

            throw ApiException(
                    apiResponse = ApiResponse(false, "사용자번호 혹은 이메일이 이미 존재합니다."),
                    status = HttpStatus.CONFLICT)
        }

        val user = User(
                roles = mutableSetOf(roleRepository.findByRole(role)
                        ?: throw AppException("Role not Set.").apply { logger.error("${role}이 DB에 없습니다.") }),
                userNumber = signUpRequest.userNumber,
                password = passwordEncoder.encode(signUpRequest.password),
                name = signUpRequest.name,
                email = signUpRequest.email,
                enabled = role == RoleName.ROLE_USER,
                hint = signUpRequest.hint,
                hintAnswer = signUpRequest.hintAnswer
        )

        val result = userRepository.save(user)

        logger.info("$role ${signUpRequest.userNumber} ${signUpRequest.name} is registered successful!")

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.userNumber).toUri()
    }
}