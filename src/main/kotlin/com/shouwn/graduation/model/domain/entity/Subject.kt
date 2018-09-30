package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import com.shouwn.graduation.model.domain.type.SubjectType
import com.shouwn.graduation.model.domain.type.SubjectTypeConverter
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Subject constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Property
        @Convert(SubjectTypeConverter::class)
        var type: SubjectType, // 이 과목의 만족 조건을 나타냄

        @Property
        var content: String, // 과목의 만족 조건에 따른 내용을 나타냄

        @Property
        var value: Int // 만족될 경우 반환될 값을 나타냄 e.g.) 과목 학점
) : UserDateAudit()