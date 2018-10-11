package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.ApiResponse
import com.shouwn.graduation.model.domain.dto.LoginRequest
import com.shouwn.graduation.model.domain.dto.SignUpRequest
import com.shouwn.graduation.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api/auth")
class AuthController @Autowired constructor(
        private val authService: AuthService
){

    @PostMapping("signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*>{

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.authenticateUser(loginRequest))
    }

    @PostMapping("signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*>{

        return ResponseEntity.created(authService.registerUser(signUpRequest))
                .body(ApiResponse(true, "User registered successfully"))
    }
}