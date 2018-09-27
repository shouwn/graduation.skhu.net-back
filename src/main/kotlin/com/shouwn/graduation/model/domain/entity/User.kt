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
        var nickname: String,

        @Property(name = "email")
        var email: String,

        @Property(name = "username")
        private var username: String,

        @Property(name = "password")
        private var password: String,

        @Property(name = "accountNonExpired")
        var accountNonExpired: Boolean = true,

        @Property(name = "accountNonLocked")
        var accountNonLocked: Boolean = true,

        @Property(name = "credentialsNonExpired")
        var credentialsNonExpired: Boolean = true,

        @Property(name = "enabled")
        var enabled: Boolean = true,

        @Relationship(type = "HAS", direction = Relationship.OUTGOING)
        var authorities: MutableSet<Authority>

) : UserDetails {

    override fun getAuthorities(): MutableCollection<SimpleGrantedAuthority>
            = authorities.asSequence().map { SimpleGrantedAuthority(it.authority.name) }.toMutableSet()

    override fun isEnabled(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun getPassword(): String = password

    fun setPassword(password: String) {
        this.password = password
    }

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun getUsername(): String = username

    fun setUsername(username: String) {
        this.username = username
    }
}