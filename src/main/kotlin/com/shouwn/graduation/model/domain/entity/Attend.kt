package com.shouwn.graduation.model.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import com.shouwn.graduation.model.domain.type.*
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert
import java.io.Serializable
import java.time.LocalDateTime

@RelationshipEntity(type = "ATTEND")
data class Attend constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        var year: Int,

        @Convert(TermTypeConverter::class)
        var term: TermType,

        @Convert(GradeTypeConverter::class)
        var grade: GradeType,

        @Convert(SectionTypeConverter::class)
        var section: SectionType
) : UserDateAudit() {

        @JsonIgnore
        @StartNode
        var user: User? = null

        @EndNode
        var course: Course? = null
}