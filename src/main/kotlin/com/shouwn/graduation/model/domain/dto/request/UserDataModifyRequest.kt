package com.shouwn.graduation.model.domain.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserDataModifyRequest (

        @get:NotBlank
        @get:Size(min = 6, max = 20)
        val password: String,

        @get:NotBlank
        @get:Size(max = 20)
        var name: String,

        @get:NotBlank
        @get:Size(max = 40)
        var email: String,

        @get:NotBlank
        @get:Size(max = 50)
        var hint: Long,

        var parties: List<Long>,

        @get:NotBlank
        @get:Size(max = 50)
        var hintAnswer: String
)