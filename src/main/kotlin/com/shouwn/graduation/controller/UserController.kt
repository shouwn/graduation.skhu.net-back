package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.AuthenticationRequest
import com.shouwn.graduation.model.domain.dto.AuthenticationToken
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("user")
class UserController @Autowired constructor(
        val authenticationManager: AuthenticationManager,
        val userService: UserService
){
    @PostMapping("login")
    fun login(@RequestBody authenticationRequest: AuthenticationRequest,
              session: HttpSession): AuthenticationToken {
        val username = authenticationRequest.username
        val password = authenticationRequest.password

        val token = UsernamePasswordAuthenticationToken(username, password)
        SecurityContextHolder.getContext().authentication = authenticationManager.authenticate(token)

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext())

        val user = userService.loadUserByUsername(username)

        return AuthenticationToken(user.username, user.authorities, session.id)
    }
}