package com.shouwn.graduation.model.domain.dto.response

data class JwtAuthenticationResponse constructor(
        var accessToken: String,
        var tokenType: String = "Bearer"
)