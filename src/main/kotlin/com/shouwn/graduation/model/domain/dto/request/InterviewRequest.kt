package com.shouwn.graduation.model.domain.dto.request

import javax.validation.constraints.NotBlank

data class  InterviewRequest constructor(

        @get: NotBlank
        var title: String,

        @get: NotBlank
        var content: String
)