package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.AuthorityType
import com.shouwn.graduation.model.domain.type.AuthorityTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@NodeEntity(label = "User")
data class User constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Property(name = "nickname")
        val nickname: String,

        @Property(name = "email")
        val email: String,

        @Property(name = "username")
        private val username: String,

        @Property(name = "password")
        private val password: String,

        @Property(name = "accountNonExpired")
        val accountNonExpired: Boolean = true,

        @Property(name = "accountNonLocked")
        val accountNonLocked: Boolean = true,

        @Property(name = "credentialsNonExpired")
        val credentialsNonExpired: Boolean = true,

        @Property(name = "enabled")
        val enabled: Boolean = true,

        @Relationship(type = "HAS", direction = Relationship.OUTGOING)
        private val authorities: MutableSet<Authority>

) : UserDetails {

    override fun getAuthorities(): List<SimpleGrantedAuthority>
            = authorities.asSequence().map { SimpleGrantedAuthority(it.authority.name) }.toList()

    override fun isEnabled(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun getUsername(): String = username
}