package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.PasswordModifyRequest
import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.response.StudentReponse
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.info
import com.shouwn.graduation.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.streams.toList

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val partyService: PartyService,
        private val passwordEncoder: PasswordEncoder
){

    @Value("\${year.first}")
    private var firstYear: Long? = null

    @Value("\${year.second}")
    private var secondYear: Long? = null

    @Value("\${year.third}")
    private var thirdYear: Long? = null

    @Value("\${year.fourth}")
    private var fourthYear: Long? = null

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

    fun student(user: User) =
            user.apply {
                if(user.role != RoleName.ROLE_STUDENT)
                    throw ApiException(
                            status = HttpStatus.PRECONDITION_FAILED,
                            apiResponse = ApiResponse(
                                    success = false,
                                    message = "${user.id}는 학생이 아닙니다."
                            )
                    )
            }.let {
                StudentReponse(
                        id = it.id!!,
                        userNumber = it.userNumber,
                        year = calStudentYearByAttends(it.attends!!),
                        name = it.name,
                        email = it.email,
                        role = it.role,
                        parties = it.parties!!
                )
            }

    fun calStudentYearByAttends(attends: MutableSet<Attend>): Long {
        val grade: Double = try{
            attends.asSequence()
                    .map { it.credit }
                    .reduce { acc, d ->  acc + d }
        } catch (e: UnsupportedOperationException){
            0.0
        }

        if(grade < firstYear!!)
            return 1

        if(grade < secondYear!!)
            return 2

        if(grade < thirdYear!!)
            return 3

        return 4
    }


    fun findUserByNotEnabled(): List<User> =
            userRepository.findAllByEnabled(false).stream()
                    .sorted(Comparator.comparing(User::userNumber))
                    .toList()

    fun userSetEnable(id: Long, user: UserPrincipal) {
        logger.info("${user.id} 에 의해 $id 유저 활성화")

        userRepository.userSetEnable(id)
    }

    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }
}