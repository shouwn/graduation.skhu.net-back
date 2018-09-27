//package com.shouwn.graduation.service
//
//import com.shouwn.graduation.model.domain.entity.User
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.security.authentication.AuthenticationProvider
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.stereotype.Component
//
//@Component
//class MyAuthenticationProvider @Autowired constructor(
//        val userService: UserService
//) : AuthenticationProvider {
//    override fun authenticate(authentication: Authentication?): Authentication {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    private fun authenticate(username: String, password: String): Authentication? =
//        userService.login(username, password)?.let {
//            MyAuthentication(username, password, listOf(SimpleGrantedAuthority(it.authority.name)), it)
//        }
//
//    override fun supports(authentication: Class<*>?): Boolean =
//            authentication == UsernamePasswordAuthenticationToken::class
//
//    class MyAuthentication(
//            username: String,
//            password: String,
//            grantedAuthorities: List<out GrantedAuthority>,
//            val user: User
//    ) : UsernamePasswordAuthenticationToken(username, password, grantedAuthorities) {
//        companion object {
//            const val serialVersionUID = 1L
//        }
//    }
//}