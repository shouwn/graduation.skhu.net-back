package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.GradeType
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "ATTEND")
data class Attend constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        var year: Int,

        var grade: GradeType,

        @StartNode
        var user: User,

        @EndNode
        var course: Course

)