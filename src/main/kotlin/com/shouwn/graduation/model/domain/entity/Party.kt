package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.model.domain.type.BelongTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Party constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Index(unique = true)
        var name: String? = null,

        @Convert(BelongTypeConverter::class)
        var belong: BelongType? = null

) : UserDateAudit()