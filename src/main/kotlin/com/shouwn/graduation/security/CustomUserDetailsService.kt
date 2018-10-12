package com.shouwn.graduation.security

import com.shouwn.graduation.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@EnableAsync(proxyTargetClass = true) // CGLib-base 사용하기 위한 어노테이션
@EnableCaching
class CustomUserDetailsService @Autowired constructor(
        val userRepository: UserRepository
): UserDetailsService{

    @Transactional
    override fun loadUserByUsername(userNumberOrEmail: String): UserDetails =
            userRepository.findByUserNumberOrEmail(userNumberOrEmail, userNumberOrEmail)?.let { UserPrincipal.create(it) }
                    ?: throw UsernameNotFoundException("User not found with username or email : $userNumberOrEmail")

    fun loadUserById(id: Long): UserDetails =
            userRepository.findById(id).let {
                UserPrincipal.create(it.orElseThrow { UsernameNotFoundException("User not found with id : $id") })
            }
}