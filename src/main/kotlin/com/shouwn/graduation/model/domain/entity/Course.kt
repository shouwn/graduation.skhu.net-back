package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType
import org.apache.tomcat.jni.Local
import org.neo4j.ogm.annotation.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import java.io.Serializable
import java.time.LocalDateTime

@NodeEntity
data class Course constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Index(unique = true) // 유니크 조건
        var code: String, // 강의 코드

        @Index // 인덱스 설정
        var name: String? = null, // 강의 이름

        @Index
        var enabled: Boolean? = null, // 폐강인지 아닌지 표현

        @Relationship(type = "REPLACE", direction = Relationship.OUTGOING)
        var replacement: Course? = null, // 대체 과목을 표현

        @Relationship(type = "OPEN", direction = Relationship.INCOMING)
        var parties: Set<Party>? = null // 어떤 소속에서 개설 되었는지 표현

) : UserDateAudit()