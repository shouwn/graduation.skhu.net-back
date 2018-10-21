package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import org.neo4j.ogm.annotation.*
import javax.validation.constraints.NotBlank

@NodeEntity
data class Interview constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        var title: String,

        var content: String,

        @Relationship(type = "WRITE", direction = Relationship.INCOMING)
        var writer: User?,

        @Relationship(type = "ASK", direction = Relationship.INCOMING)
        var asker: User?

) : UserDateAudit()