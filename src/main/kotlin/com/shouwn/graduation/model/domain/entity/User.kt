package com.shouwn.graduation.model.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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

        @JsonIgnore
        @Property(name = "username")
        var username: String,

        @JsonIgnore
        @Property(name = "password")
        var password: String,

        @JsonIgnore
        @Relationship(type = "HAS", direction = Relationship.OUTGOING)
        var roles: MutableSet<Role>,

        @JsonIgnore
        @Property(name = "enabled")
        var enabled: Boolean
) : DateAudit()