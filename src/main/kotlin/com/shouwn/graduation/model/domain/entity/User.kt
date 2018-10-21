package com.shouwn.graduation.model.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.shouwn.graduation.model.domain.entity.audit.DateAudit
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.model.domain.type.RoleNameConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

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

        @JsonIgnore
        var hintAnswer: String,

        @JsonIgnore
        var enabled: Boolean,

        @JsonIgnore
        @Convert(RoleNameConverter::class)
        var role: RoleName,

        @Relationship(type = "ATTEND", direction = Relationship.OUTGOING)
        var attend: MutableSet<Attend>? = null
  
) : DateAudit()