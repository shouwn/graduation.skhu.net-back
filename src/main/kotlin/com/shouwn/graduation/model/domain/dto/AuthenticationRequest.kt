package com.shouwn.graduation.model.domain.dto

data class AuthenticationRequest constructor(
        var username: String,
        var password: String
)