package com.shouwn.graduation.model.domain.dto.request

import javax.validation.constraints.NotBlank

data class LoginRequest constructor(
        @get:NotBlank
        var userNumberOrEmail: String,

        @get:NotBlank
        var password: String
)