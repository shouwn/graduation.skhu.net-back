package com.shouwn.graduation

import com.shouwn.graduation.model.domain.entity.Authority
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.type.AuthorityType
import com.shouwn.graduation.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserRepositoryTest @Autowired constructor(
        val userRepository: UserRepository
){

    @Test
    fun findUserTest() {
        val user = User(
                nickname = "tester",
                email = "test@gmail.com",
                username = "test",
                password = "test123",
                authorities = mutableSetOf(Authority(authority = AuthorityType.ROLE_GUEST))
        )

        val saveUser = userRepository.save(user)
        userRepository.deleteAll()
        Assertions.assertEquals(user.copy(id = saveUser.id), saveUser)
    }
}