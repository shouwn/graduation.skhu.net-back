package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InfoController(
        val env: Environment
) {

    @GetMapping("profile")
    fun profile() =
         env.activeProfiles.firstOrNull()
}