package com.shouwn.graduation.utils

import com.shouwn.graduation.security.UserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger =
        LoggerFactory.getLogger(T::class.java)

fun UserPrincipal.info(): String =
        "권한: ${this.entity.role.name} 아이디: ${this.id}"