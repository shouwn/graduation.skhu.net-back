package com.shouwn.graduation.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
class TestController(
        val env: Environment
) {

    @GetMapping("hello")
    fun hello(): String {
        return "hello"
    }

    @GetMapping("profile")
    fun profile(): String? {
        return env.activeProfiles.firstOrNull()
    }

}