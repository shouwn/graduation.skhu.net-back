package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.PasswordModifyRequest
import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
class UserController @Autowired constructor(
        val userService: UserService
){
    @PutMapping
    fun modifyUserData(@CurrentUser user: UserPrincipal,
                       @RequestBody userDataModifyRequest: UserDataModifyRequest) =
            ResponseEntity.ok(userService.modifyUserData(user, userDataModifyRequest))

    @GetMapping
    fun findUserData(@CurrentUser user: UserPrincipal) =
            ResponseEntity.ok(user.entity)

    @PutMapping("password")
    fun modifyPassword(@CurrentUser user: UserPrincipal,
                       @RequestBody passwordModifyRequest: PasswordModifyRequest) =
            ResponseEntity.ok(userService.modifyPassword(user, passwordModifyRequest).let { "비밀번호 변경 완료" })

    @GetMapping("student")
    @Secured("ROLE_STUDENT")
    fun findSelfStudentData(@CurrentUser user: UserPrincipal) =
            ResponseEntity.ok(userService.student(user.entity))

}