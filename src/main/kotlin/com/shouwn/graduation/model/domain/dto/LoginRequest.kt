package com.shouwn.graduation.model.domain.dto

import javax.validation.constraints.NotBlank

data class LoginRequest constructor(
        @NotBlank
        var usernameOrEmail: String,

        @NotBlank
        var password: String
)