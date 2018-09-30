package com.shouwn.graduation.model.domain.dto

data class JwtAuthenticationResponse constructor(
        var accessToken: String,

        var tokenType: String = "Bearer"
)