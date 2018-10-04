package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.DateAudit
import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import org.neo4j.ogm.annotation.*

@NodeEntity
data class Interview constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Property(name = "title")
        var title: String,

        @Property(name = "content")
        var content: String

) : UserDateAudit()