package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class AdminService @Autowired constructor(
        val userRepository: UserRepository
){
    private val logger = LoggerFactory.getLogger(AdminService::class.java)

    fun findUserByNotEnabled(): List<User> =
            userRepository.findAllByEnabled(false).stream()
                    .sorted(Comparator.comparing(User::userNumber))
                    .toList()

    fun userSetEnable(id: Long, user: UserPrincipal) {
        logger.info("${user.id} 에 의해 $id 유저 활성화")

        userRepository.userSetEnable(id)
    }
}