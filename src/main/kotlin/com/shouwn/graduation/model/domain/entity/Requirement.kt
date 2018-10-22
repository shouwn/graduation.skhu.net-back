package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import com.shouwn.graduation.model.domain.type.SatisfyingType
import com.shouwn.graduation.model.domain.type.SatisfyingTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Requirement constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Index
        var name: String, // 요건의 이름

        @Convert(SatisfyingTypeConverter::class)
        var satisfying: SatisfyingType, // 이 요건이 만족되기 위한 조건이 무엇인지 나타냄

        @Index
        var need: Long, // 요건의 타입에 맞춘 요구 값을 나타냄

        @Index
        var clazz: Long, // 요건이 해당되는 학번을 가르킴 e.g.) 2013 이면 2013 ~

        @Relationship(type = "APPLY", direction = Relationship.OUTGOING)
        var parties: Set<Party>, // 요건을 만족해야 하는 소속이 어디인가를 나타냄

        @Relationship(type = "SUB", direction = Relationship.OUTGOING)
        var subs: Set<Requirement>?, // 하위 요건

        @Relationship(type = "REQUIRE", direction = Relationship.OUTGOING)
        var courses: Set<Course>? // 이 요건 아래에 과목이 있다면 과목과의 관계 표현

) : UserDateAudit()