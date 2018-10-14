package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.request.LoginRequest
import com.shouwn.graduation.model.domain.dto.request.SignUpRequest
import com.shouwn.graduation.model.domain.type.RoleName
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

    @PostMapping("user/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest) =
            ResponseEntity.created(authService.registerUser(signUpRequest, RoleName.ROLE_USER))
                    .body(ApiResponse(true, "사용자 등록 성공"))

    @PostMapping("admin/signup")
    fun registerAdmin(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*>{

        return ResponseEntity.created(authService.registerUser(signUpRequest, RoleName.ROLE_ADMIN))
                .body(ApiResponse(true, "관리자 등록 성공"))
    }
}