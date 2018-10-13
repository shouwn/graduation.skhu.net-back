package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType
import org.neo4j.ogm.annotation.*
import java.time.LocalDateTime

@NodeEntity
data class Course constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Index(unique = true)
        var code: String,

        @Index
        var name: String,

        var section: SectionType, // 이수구분 enum 예정

        var credit: Int, // 학점

        var term: TermType,

        var enabled: Boolean, // 성적 enum 예정

        @Relationship(type = "REPLACE", direction = Relationship.OUTGOING)
        var replacement: Course
)