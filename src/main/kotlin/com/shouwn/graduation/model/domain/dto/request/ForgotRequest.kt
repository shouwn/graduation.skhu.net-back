package com.shouwn.graduation.model.domain.dto.request

data class ForgotRequest (

        var userNumberOrEmail: String,

        var hintAnswer: String
)