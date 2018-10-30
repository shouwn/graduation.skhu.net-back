package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.CourseService
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("api/admins")
class AdminController @Autowired constructor(
        val userService: UserService,
        val courseService: CourseService
){

    @GetMapping("/users/wait")
    fun findWaitingUsers() =
            ResponseEntity.ok(userService.findUserByNotEnabled())

    @PutMapping("users/{id}/enabled")
    fun confirmAdmin(@PathVariable("id") id: Long,
                     @CurrentUser user: UserPrincipal): ResponseEntity<Any> =
            userService.userSetEnable(id, user).let { ResponseEntity.noContent().build() }

    @DeleteMapping("users/{userId}")
    fun deleteUser(@PathVariable("userId") userId: Long) =
            ResponseEntity.noContent().build<Any>().apply { userService.deleteUser(userId) }
}