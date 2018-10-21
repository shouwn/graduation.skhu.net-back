package com.shouwn.graduation.model.domain.dto.request

data class AuthenticationRequest constructor(
        var username: String,
        var password: String
)