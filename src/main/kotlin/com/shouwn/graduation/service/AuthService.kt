package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.UserDataDto
import com.shouwn.graduation.model.domain.dto.request.ForgotRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.response.JwtAuthenticationResponse
import com.shouwn.graduation.model.domain.dto.request.LoginRequest
import com.shouwn.graduation.model.domain.dto.request.PasswordConfirmRequest
import com.shouwn.graduation.model.domain.dto.request.SignUpRequest
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.JwtTokenProvider
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.info
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
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Service
class AuthService @Autowired constructor(
        private val authenticationManager: AuthenticationManager,
        private val tokenProvider: JwtTokenProvider,
        private val partyService: PartyService,
        private val passwordEncoder: PasswordEncoder,
        private val userService: UserService
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    @Value("\${app.signCode.user}")
    private lateinit var userSignCode: String

    @Value("\${app.signCode.admin}")
    private lateinit var adminSignCode: String

    fun checkPassword(user: UserPrincipal, request: PasswordConfirmRequest){

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(user.entity.userNumber, request.password))

        SecurityContextHolder.getContext().authentication = authentication
    }

    fun authenticateUser(loginRequest: LoginRequest): JwtAuthenticationResponse {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.userNumberOrEmail, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)

        logger.info("사용자 번호 ${loginRequest.userNumberOrEmail} 가 로그인하였습니다.")

        return JwtAuthenticationResponse(jwt)
    }

    fun authenticateUserByHint(request: ForgotRequest): JwtAuthenticationResponse {
        val user = userService.findUserByUserNumberOrEmail(request.userNumberOrEmail)
                ?: throw ApiException(
                        status = HttpStatus.NOT_FOUND,
                        apiResponse = ApiResponse(
                                success = false,
                                message = "${request.userNumberOrEmail}에 해당하는 유저가 없습니다."
                        )
                )

        if(user.hintAnswer != request.hintAnswer)
            throw ApiException(
                    status = HttpStatus.FORBIDDEN,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "답변이 올바르지 않습니다."
                    )
            )

        val jwt = tokenProvider.generateToken(user.id.toString())

        logger.info("사용자 번호 ${user.userNumber} 가 비밀번호 찾기를 통해 로그인하였습니다.")

        return JwtAuthenticationResponse(jwt)
    }

    @Transactional
    fun registerUser(signUpRequest: SignUpRequest, role: RoleName): URI {
        val signCode = if(role == RoleName.ROLE_STUDENT) userSignCode else adminSignCode

        if(signUpRequest.code != signCode)
            throw ApiException(
                    apiResponse = ApiResponse(false, "인증코드가 맞지 않습니다."),
                    status = HttpStatus.UNAUTHORIZED
            ).apply { logger.error("Register Fail: ${signUpRequest.code} is not $signCode") }

        if (userService.existsByUserNumberOrEmail(signUpRequest.userNumber))
            throw ApiException(
                    apiResponse = ApiResponse(false, "사용자번호 혹은 이메일이 이미 존재합니다."),
                    status = HttpStatus.CONFLICT
            ).apply { logger.error("Register Fail: ${signUpRequest.userNumber} or ${signUpRequest.email} is not already exist") }

        val user = userService.saveUser(User(
                role = role,
                userNumber = signUpRequest.userNumber,
                password = passwordEncoder.encode(signUpRequest.password),
                name = signUpRequest.name,
                email = signUpRequest.email,
                enabled = role == RoleName.ROLE_STUDENT,
                hint = signUpRequest.hint,
                hintAnswer = signUpRequest.hintAnswer
        ), UserDataDto(parties = signUpRequest.parties))
                .apply { logger.info("${this.info()} 사용자 이름: ${this.name} 회원 등록") }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(user.userNumber).toUri()
    }

    fun findHintByUserNumberOrEmail(userNameOrEmail: String) =
            userService.findUserByUserNumberOrEmail(userNameOrEmail)?.hint
                    ?: throw ApiException(
                            status = HttpStatus.NOT_FOUND,
                            apiResponse = ApiResponse(false,
                                    "${userNameOrEmail}에 해당하는 유저가 없습니다.")
                    )
}