package com.shouwn.graduation.model.domain.dto

import javax.validation.constraints.NotBlank

data class LoginRequest constructor(
        @get:NotBlank
        var usernameOrEmail: String,

        @get:NotBlank
        var password: String
)