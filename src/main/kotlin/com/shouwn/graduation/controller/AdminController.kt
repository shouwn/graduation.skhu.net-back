package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("api/admin")
class AdminController @Autowired constructor(
        val adminService: AdminService
){

    @GetMapping("/users/wait")
    fun findWaitingUsers() =
            ResponseEntity.ok(adminService.findUserByNotEnabled())

    @PutMapping("users/{id}/enabled")
    fun confirmAdmin(@PathVariable("id") id: Long,
                     @CurrentUser user: UserPrincipal): ResponseEntity<Any> =
            adminService.userSetEnable(id, user).let { ResponseEntity.noContent().build() }
}