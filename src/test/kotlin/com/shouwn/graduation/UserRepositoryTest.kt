package com.shouwn.graduation

import com.shouwn.graduation.model.domain.entity.Authority
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.type.AuthorityType
import com.shouwn.graduation.repository.AuthorityRepository
import com.shouwn.graduation.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserRepositoryTest @Autowired constructor(
        val userRepository: UserRepository,
        val authorityRepository: AuthorityRepository
){
    companion object {
        val user = User(
                nickname = "tester",
                email = "test@gmail.com",
                username = "test",
                password = "test123",
                authorities = hashSetOf(Authority(authority = AuthorityType.ROLE_GUEST))
        )
    }

    @Test
    fun findUserByUsernameTest(){
        userRepository.deleteAll()

        println(user)

        val saveUser = userRepository.save(user)
        val findUser = userRepository.findByUsername(saveUser.username)

        userRepository.deleteAll()
        authorityRepository.deleteAll()

        println(findUser?.authorities == saveUser.authorities)
        println(saveUser.authorities == findUser?.authorities) // 이건 false 임... 왜?

        Assertions.assertAll(
                Executable { Assertions.assertEquals(saveUser.id, findUser?.id) },
                Executable { Assertions.assertEquals(saveUser.nickname, findUser?.nickname) },
                Executable { Assertions.assertEquals(saveUser.email, findUser?.email) },
                Executable { Assertions.assertEquals(saveUser.username, findUser?.username) },
                Executable { Assertions.assertEquals(saveUser.password, findUser?.password) },
                Executable { Assertions.assertEquals(saveUser.accountNonExpired, findUser?.accountNonExpired) },
                Executable { Assertions.assertEquals(saveUser.accountNonLocked, findUser?.accountNonLocked) },
                Executable { Assertions.assertEquals(saveUser.credentialsNonExpired, findUser?.credentialsNonExpired) },
                Executable { Assertions.assertEquals(saveUser.enabled, findUser?.enabled) },
                Executable { Assertions.assertEquals(findUser?.authorities, saveUser.authorities) },
                Executable { Assertions.assertEquals(saveUser.authorities.first(), findUser?.authorities?.first()) }
        )
    }
}