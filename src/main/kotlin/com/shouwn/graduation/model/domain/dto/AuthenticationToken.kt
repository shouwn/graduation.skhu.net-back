package com.shouwn.graduation.model.domain.dto

import org.springframework.security.core.GrantedAuthority

data class AuthenticationToken (
        var username: String,
        var authorities: MutableCollection<out Any>,
        var token: String
)