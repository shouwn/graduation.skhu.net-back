package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository
){
    fun modifyUserData(user: UserPrincipal, request: UserDataModifyRequest) =
            userRepository.save(user.entity.copy(
                    password = request.password,
                    name = request.name,
                    email = request.email,
                    hint = request.hint,
                    hintAnswer = request.hintAnswer
            ))

}