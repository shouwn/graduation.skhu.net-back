package com.shouwn.graduation.model.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.shouwn.graduation.model.domain.type.*
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert
import java.io.Serializable
import java.time.LocalDateTime

@RelationshipEntity(type = "ATTEND")
data class Attend constructor(

        @Id @GeneratedValue
        var relationshipId: Long? = null,

        var year: Int,

        @Property(name = "term")
        @Convert(TermTypeConverter::class)
        var term: TermType,

        @Convert(GradeTypeConverter::class)
        var grade: GradeType,

        @Convert(SectionTypeConverter::class)
        var section: SectionType,

        @JsonIgnore
        @StartNode
        var user: User,

        @EndNode
        var course: Course,

        var createdBy: Long,

        var updatedBy: Long,

        var createdAt: LocalDateTime = LocalDateTime.now(),

        var updatedAt: LocalDateTime = LocalDateTime.now()
) : Serializable