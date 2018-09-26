package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.utils.Encryption
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
        val userRepository: UserRepository
){

    fun login(username: String, password: String): User? =
        userRepository.findByUsername(username)?.let {
            if(it.password == Encryption.encrypt(password, Encryption.MD5)) it else null
        }
}