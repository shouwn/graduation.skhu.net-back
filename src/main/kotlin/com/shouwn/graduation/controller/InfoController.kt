package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.type.AuthorityType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InfoController(
        val env: Environment
) {

    @GetMapping("profile")
    fun profile(): String? {
        return env.activeProfiles.firstOrNull()
    }

    @Secured(AuthorityType.ROLE_GUEST)
    @GetMapping("guest/hello")
    fun hello(): String = "Hello Guest"

    @Secured(AuthorityType.ROLE_USER)
    @GetMapping("user/hello")
    fun hello2(): String = "Hello User"
}