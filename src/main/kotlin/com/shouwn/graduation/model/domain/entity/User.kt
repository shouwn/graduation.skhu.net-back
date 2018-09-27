package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.AuthorityType
import com.shouwn.graduation.model.domain.type.AuthorityTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@NodeEntity
data class User constructor(
        @Id @GeneratedValue
        val id: Long,

        @Property
        val nickname: String,

        @Property
        val email: String,

        @Property
        private val username: String,

        @Property
        private val password: String,

        @Property
        val accountNonExpired: Boolean,

        @Property
        val accountNonLocked: Boolean,

        @Property
        val credentialsNonExpired: Boolean,

        @Property
        val enabled: Boolean,

        @Relationship(type = "element", direction = Relationship.OUTGOING)
        private val authorities: MutableSet<Authority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities.map { SimpleGrantedAuthority(it.authority.name) }.toList() // 수정 필요

    override fun isEnabled(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun getUsername(): String = username
}