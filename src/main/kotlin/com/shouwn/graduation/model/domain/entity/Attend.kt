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

        var year: Long, // 몇 년도에 수강했는지 표현

        @Convert(TermTypeConverter::class)
        var term: TermType, // 무슨 학기에 수강했는지 표현

        @Convert(GradeTypeConverter::class)
        var grade: GradeType, // 성적

        @Convert(SectionTypeConverter::class)
        var section: SectionType, // 이수 구분

        @Convert(AttendTypeConverter::class)
        var type: AttendType // 수강한 과목인지, 수강할 과목인지 나타내는 속성

) : UserDateAudit() {

        @JsonIgnore
        @StartNode
        var user: User? = null // 학생과의 관계

        @EndNode
        var course: Course? = null // 과목과의 관계
}