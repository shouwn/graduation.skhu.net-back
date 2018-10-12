package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.entity.Requirement
import com.shouwn.graduation.model.domain.type.SatisfyingType
import com.shouwn.graduation.repository.RequirementRepository
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
class TestController @Autowired constructor(
    val requirementRepository: RequirementRepository
){

    @Secured("ROLE_ADMIN")
    @GetMapping("guest/hello")
    fun hello(): String = "Hello Guest"

    @Secured("ROLE_USER")
    @GetMapping("user/hello")
    fun hello2(): String = "Hello User"

    @GetMapping("currentUser")
    fun currentUser(@CurrentUser user: UserPrincipal): UserPrincipal = user
}