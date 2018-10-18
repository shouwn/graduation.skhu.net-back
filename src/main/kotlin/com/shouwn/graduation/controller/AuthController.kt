package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.request.LoginRequest
import com.shouwn.graduation.model.domain.dto.request.SignUpRequest
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
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

    @PostMapping("student/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest) =
            ResponseEntity.created(authService.registerUser(signUpRequest, RoleName.ROLE_STUDENT))
                    .body(ApiResponse(true, "학생 등록 성공"))

    @PostMapping("admin/signup")
    fun registerAdmin(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*>{

        return ResponseEntity.created(authService.registerUser(signUpRequest, RoleName.ROLE_ADMIN))
                .body(ApiResponse(true, "교직원 등록 성공"))
    }

    @GetMapping("user/{userNumberOrEmail}/hint")
    fun findHint(@PathVariable("userNumberOrEmail") userNumberOrEmail: String) =
            ResponseEntity.ok(authService.findHintByUserNumberOrEmail(userNumberOrEmail))

    @PostMapping("test")
    fun test(@RequestBody request: AttendRequest) =
            request

    @GetMapping("test2")
    fun test2() = LocalDateTime.now()!!
}