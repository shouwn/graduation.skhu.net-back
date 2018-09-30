package com.shouwn.graduation.model.domain.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignUpRequest constructor(

        @get:NotBlank
        @get:Size(min = 4, max = 40)
        var name: String,

        @get:NotBlank
        @get:Size(min = 3, max = 15)
        var username: String,

        @get:NotBlank
        @get:Size(max = 40)
        var email: String,

        @get:NotBlank
        @get:Size(min = 6, max = 20)
        var password: String
)