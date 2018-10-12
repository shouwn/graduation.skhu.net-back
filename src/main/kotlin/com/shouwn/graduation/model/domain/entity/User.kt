package com.shouwn.graduation.model.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.shouwn.graduation.model.domain.entity.audit.DateAudit
import org.neo4j.ogm.annotation.*

@NodeEntity(label = "User")
data class User constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        var userNumber: String,

        @JsonIgnore
        var password: String,

        var name: String,

        var email: String,

        var hint: String,

        var hintAnswer: String,

        @JsonIgnore
        var enabled: Boolean,

        @JsonIgnore
        @Relationship(type = "HAS", direction = Relationship.OUTGOING)
        var roles: MutableSet<Role>

) : DateAudit()