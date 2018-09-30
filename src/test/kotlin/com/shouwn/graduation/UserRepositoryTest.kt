//package com.shouwn.graduation
//
//import com.shouwn.graduation.model.domain.entity.Authority
//import com.shouwn.graduation.model.domain.entity.User
//import com.shouwn.graduation.model.domain.type.AuthorityType
//import com.shouwn.graduation.repository.AuthorityRepository
//import com.shouwn.graduation.repository.UserRepository
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.junit.jupiter.api.function.Executable
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit.jupiter.SpringExtension
//
//@ExtendWith(SpringExtension::class)
//@SpringBootTest
//class UserRepositoryTest @Autowired constructor(
//        val userRepository: UserRepository,
//        val authorityRepository: AuthorityRepository
//){
//    companion object {
//        val user = User(
//                name = "tester",
//                email = "test@gmail.com",
//                username = "test",
//                password = "test123",
//                roles = mutableSetOf(Authority(authority = AuthorityType.GUEST))
//        )
//    }
//
//    @Test
//    fun findUserByUsernameTest(){
//        userRepository.deleteAll()
//
//        println(user)
//
//        val saveUser = userRepository.save(user)
//        val findUser = userRepository.findByUsernameOrEmail(saveUser.username)
//
//        userRepository.deleteAll()
//        authorityRepository.deleteAll()
//
//        println(findUser?.authorities == saveUser.authorities)
//        println(saveUser.authorities == findUser?.authorities) // 이건 false 임... 왜?
//
//        Assertions.assertAll(
//                Executable { Assertions.assertEquals(saveUser.id, findUser?.id) },
//                Executable { Assertions.assertEquals(saveUser.name, findUser?.name) },
//                Executable { Assertions.assertEquals(saveUser.email, findUser?.email) },
//                Executable { Assertions.assertEquals(saveUser.username, findUser?.username) },
//                Executable { Assertions.assertEquals(saveUser.password, findUser?.password) },
//                Executable { Assertions.assertEquals(findUser?.authorities, saveUser.authorities) },
//                Executable { Assertions.assertEquals(saveUser.authorities.first(), findUser?.authorities?.first()) }
//        )
//    }
//}