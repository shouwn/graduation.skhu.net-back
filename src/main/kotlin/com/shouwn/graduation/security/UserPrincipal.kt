package com.shouwn.graduation.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.shouwn.graduation.model.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal constructor(
        var id: Long,

        var name: String,

        @JsonIgnore
        var email: String,

        private var username: String,

        @JsonIgnore
        private var password: String,

        private var authorities: MutableCollection<out GrantedAuthority>
) : UserDetails {

    companion object {
        fun create(user: User): UserPrincipal =
                UserPrincipal(
                        id = user.id!!,
                        name = user.name,
                        email = user.email,
                        username = user.username,
                        password = user.password,
                        authorities = user.roles.asSequence()
                                .map { SimpleGrantedAuthority(it.role.name) }
                                .toMutableList()
                )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isEnabled(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
}