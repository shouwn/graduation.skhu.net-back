package com.shouwn.graduation.model.entity

import com.shouwn.graduation.model.domain.AuthorityType
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class User constructor(
        @Id @GeneratedValue
        val id: Long,

        @Property
        val nickname: String,

        @Property
        val email: String,

        @Property
        val username: String,

        @Property
        val password: String,

        @Property
        val accountNonExpired: Boolean,

        @Property
        val accountNonLocked: Boolean,

        @Property
        val credentialsNonExpired: Boolean,

        @Property
        val enabled: Boolean,

        @Convert()
        val authority: AuthorityType
)