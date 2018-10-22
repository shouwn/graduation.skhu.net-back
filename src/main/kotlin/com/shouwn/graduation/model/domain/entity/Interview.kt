package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import org.neo4j.ogm.annotation.*
import javax.validation.constraints.NotBlank

@NodeEntity
data class Interview constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        var title: String, // 메모의 제목

        var content: String, // 메모 내용

        @Relationship(type = "WRITE", direction = Relationship.INCOMING)
        var writer: User?, // 작성자와의 관계

        @Relationship(type = "ASK", direction = Relationship.OUTGOING)
        var asker: User? // 작성 대상인 학생과의 관계

) : UserDateAudit()