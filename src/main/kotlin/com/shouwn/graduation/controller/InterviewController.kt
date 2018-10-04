package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/interview")
class InterviewController{

    @PostMapping("user/{userId}")
    fun postInterview(@PathVariable("userId") userId: Int?, @CurrentUser user: UserPrincipal?){

    }
}