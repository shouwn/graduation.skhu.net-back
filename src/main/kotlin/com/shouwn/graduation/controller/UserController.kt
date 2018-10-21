package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/user")
class UserController @Autowired constructor(
        val userService: UserService
){
    @PutMapping
    fun modifyUserData(@CurrentUser user: UserPrincipal,
                       @RequestBody userDataModifyRequest: UserDataModifyRequest) =
            ResponseEntity.ok(userService.modifyUserData(user, userDataModifyRequest))
}