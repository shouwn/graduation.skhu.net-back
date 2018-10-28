package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.PasswordModifyRequest
import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.info
import com.shouwn.graduation.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val partyService: PartyService,
        private val passwordEncoder: PasswordEncoder
){

    private val logger = logger()

    fun modifyUserData(user: UserPrincipal, request: UserDataModifyRequest) =
            userRepository.updateUser(
                    userId = user.id,
                    password = passwordEncoder.encode(request.password),
                    name = request.name,
                    email = request.email,
                    hint = request.hint,
                    hintAnswer = request.hintAnswer,
                    updatedAt = LocalDateTime.now().toString(),
                    parties = partyService.findPartiesByPartyIds(request.parties)
                            .apply { filter { it.belong == BelongType.GENERAL }
                                    .forEach { _ ->
                                        throw ApiException(
                                                status = HttpStatus.PRECONDITION_FAILED,
                                                apiResponse = ApiResponse(
                                                        success = false,
                                                        message = "교양 소속으로는 선택할 수 없습니다."
                                                )
                                        ) }
                                println(this)
                            }
            ).apply { logger.info("${user.info()} 가 개인 정보를 변경") }

    fun modifyPassword(user: UserPrincipal, request: PasswordModifyRequest) =
            userRepository.modifyPassword(
                    user.id,
                    passwordEncoder.encode(request.password),
                    LocalDateTime.now().toString()
            ).apply { logger.info("${user.info()} 가 비밀번호를 변경") }

    fun findUsersById(ids: Iterable<Long>) =
            findAllById(userRepository, ids)

}