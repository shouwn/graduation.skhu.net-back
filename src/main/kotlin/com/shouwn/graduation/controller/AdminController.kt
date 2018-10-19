package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.AdminService
import com.shouwn.graduation.service.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("api/admin")
class AdminController @Autowired constructor(
        val adminService: AdminService,
        val courseService: CourseService
){

    @GetMapping("/users/wait")
    fun findWaitingUsers() =
            ResponseEntity.ok(adminService.findUserByNotEnabled())

    @PutMapping("users/{id}/enabled")
    fun confirmAdmin(@PathVariable("id") id: Long,
                     @CurrentUser user: UserPrincipal): ResponseEntity<Any> =
            adminService.userSetEnable(id, user).let { ResponseEntity.noContent().build() }


}