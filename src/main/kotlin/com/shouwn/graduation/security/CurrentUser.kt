package com.shouwn.graduation.security

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@AuthenticationPrincipal
annotation class CurrentUser {
}