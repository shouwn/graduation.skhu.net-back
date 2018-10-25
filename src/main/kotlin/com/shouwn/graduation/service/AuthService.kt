package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.ForgotRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.response.JwtAuthenticationResponse
import com.shouwn.graduation.model.domain.dto.request.LoginRequest
import com.shouwn.graduation.model.domain.dto.request.SignUpRequest
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.model.domain.type.RoleName
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
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Service
class AuthService @Autowired constructor(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository,
        private val tokenProvider: JwtTokenProvider,
        private val partyService: PartyService
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

        logger.info("사용자 번호 ${loginRequest.userNumberOrEmail} 가 로그인하였습니다.")

        return JwtAuthenticationResponse(jwt)
    }

    fun authenticateUserByHint(request: ForgotRequest): JwtAuthenticationResponse {
        val user = userRepository.findByUserNumberOrEmail(request.userNumberOrEmail, request.userNumberOrEmail) ?: throw ApiException(
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
                            message = "${request.hintAnswer}가 올바르지 않습니다."
                    )
            )

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(user.userNumber, user.password))

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)

        logger.info("사용자 번호 ${user.userNumber} 가 비밀번호 찾기를 통해 로그인하였습니다.")

        return JwtAuthenticationResponse(jwt)
    }

    @Transactional
    fun registerUser(signUpRequest: SignUpRequest, role: RoleName): URI {
        val signCode = if(role == RoleName.ROLE_STUDENT) userSignCode else adminSignCode

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

        var parties = partyService.findPartiesByPartyIds(signUpRequest.parties)
                .apply { filter { it.belong == BelongType.GENERAL }
                        .forEach { _ ->
                            throw ApiException(
                                status = HttpStatus.PRECONDITION_FAILED,
                                apiResponse = ApiResponse(
                                        success = false,
                                        message = "교양 소속으로는 선택할 수 없습니다."
                                )
                        ) }
                }

        val user = User(
                role = role,
                userNumber = signUpRequest.userNumber,
                password = passwordEncoder.encode(signUpRequest.password),
                name = signUpRequest.name,
                email = signUpRequest.email,
                enabled = role == RoleName.ROLE_STUDENT,
                hint = signUpRequest.hint,
                hintAnswer = signUpRequest.hintAnswer
        ).apply {
            this.parties = parties
            createDateAudit()
        }

        val result = userRepository.save(user)

        logger.info("권한: $role ${signUpRequest.userNumber}, 사용자 번호: ${signUpRequest.userNumber}, " +
                "사용자 이름: ${signUpRequest.name} 회원 등록")

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.userNumber).toUri()
    }

    fun findHintByUserNumberOrEmail(userNameOrEmail: String) =
            userRepository.findByUserNumberOrEmail(userNameOrEmail, userNameOrEmail)?.hint
                    ?: throw ApiException(
                            status = HttpStatus.NOT_FOUND,
                            apiResponse = ApiResponse(false,
                                    "${userNameOrEmail}에 해당하는 유저가 없습니다.")
                    )
}