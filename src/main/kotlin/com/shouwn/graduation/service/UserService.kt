package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.UserData
import com.shouwn.graduation.model.domain.dto.request.PasswordModifyRequest
import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.response.UserResponse
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.model.domain.type.SearchType
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.info
import com.shouwn.graduation.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.streams.toList

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val partyService: PartyService,
        private val passwordEncoder: PasswordEncoder,
        private val yearService: YearService
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

    fun userData(user: User) =
            user.let {
                UserData().apply {
                        id = it.id!!
                        userNumber = it.userNumber
                        credit =
                        if (it.attends?.isEmpty() == true) 0.0
                        else it.attends!!.asSequence()
                                .map { attend -> attend.credit }
                                .reduce { acc, d -> acc + d }
                        name = it.name
                        email = it.email
                        role = it.role
                        parties = it.parties!!
                }
            }

    fun findUserByNotEnabled(): List<User> =
            userRepository.findAllByEnabled(false).stream()
                    .sorted(Comparator.comparing(User::userNumber))
                    .toList()

    fun userSetEnable(id: Long, user: UserPrincipal)  =
            userRepository.userSetEnable(id).apply {
                logger.info("${user.id} 에 의해 $id 유저 활성화")
            }

    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    fun findUserBySearching(type: SearchType, searchTxt: String, role: RoleName, partyId: Long, year: Int, page: Int, size: Int) =
            findAllUserBySearching(type, searchTxt, role, partyId, year, page, size)
                    .map { userResponse(it) }

    fun userResponse(userData: UserData) =
            userData.let {
                UserResponse(
                        id = it.id,
                        userNumber = it.userNumber,
                        year = yearService.getYear(it.credit).toLong(),
                        name = it.name,
                        email = it.email,
                        role = it.role,
                        parties = it.parties
                )
            }

    private fun findAllUserBySearching(type: SearchType, searchTxt: String, role: RoleName, partyId: Long, year: Int, page: Int, size: Int) =
            userRepository.findAllBySearch(
                    name = if(type != SearchType.ALL && type == SearchType.NAME) ".*$searchTxt.*" else ".*",
                    userNumber = if(type != SearchType.ALL && type == SearchType.NUMBER) ".*$searchTxt.*" else ".*",
                    role = role.value,
                    partyId = partyId,
                    maxCredit = if(year > 0) yearService.getCompletionCredit(year).toLong() else 9999,
                    minCredit = if(year >= 1) yearService.getCompletionCredit(year - 1).toLong() else 0,
                    pageable = PageRequest.of(page, size)
            )
}