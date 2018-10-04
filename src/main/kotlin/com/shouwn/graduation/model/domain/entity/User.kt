package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.DateAudit
import org.neo4j.ogm.annotation.*

@NodeEntity(label = "User")
data class User constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Property(name = "name")
        var name: String,

        @Property(name = "email")
        var email: String,

        @Property(name = "username")
        var username: String,

        @Property(name = "password")
        var password: String,

        @Relationship(type = "HAS", direction = Relationship.OUTGOING)
        var roles: MutableSet<Role>,

        @Property(name = "enabled")
        var enabled: Boolean
) : DateAudit()