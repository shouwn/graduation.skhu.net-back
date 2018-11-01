package com.shouwn.graduation.utils

import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.security.UserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger =
        LoggerFactory.getLogger(T::class.java)

fun UserPrincipal.info(): String =
        this.entity.info()

fun User.info(): String =
        "권한: ${this.role.name} 아이디: ${this.id}"