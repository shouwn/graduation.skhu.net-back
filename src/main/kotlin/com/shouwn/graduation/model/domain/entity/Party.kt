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
        var name: String? = null, // 소속 이름

        @Convert(BelongTypeConverter::class)
        var belong: BelongType? = null // 교양에 포함 되는지, 전공에 포함되는지 나타내는 속성

) : UserDateAudit() {
    @Relationship(type = "ACCEPT", direction = Relationship.OUTGOING)
    var acceptCourses: List<Course>? = null // 전공 인정 목록
}