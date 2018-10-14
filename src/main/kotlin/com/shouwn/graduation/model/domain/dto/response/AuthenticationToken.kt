package com.shouwn.graduation.model.domain.dto.response

data class AuthenticationToken (
        var username: String,
        var authorities: MutableCollection<out Any>,
        var token: String
)