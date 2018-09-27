package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.SatisfyingType
import com.shouwn.graduation.model.domain.type.SatisfyingTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Requirement constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Property
        var name: String, // 요건의 이름

        @Convert(SatisfyingTypeConverter::class)
        var satisfying: SatisfyingType, // 이 요건이 만족되기 위한 조건이 무엇인지 나타냄

        @Property
        var require: Int, // 요건의 타입에 맞춘 요구 값을 나타냄

        @Relationship(type = "child", direction = Relationship.OUTGOING)
        var children: Set<Requirement>, // 하위 요건

        @Relationship(type = "element", direction = Relationship.OUTGOING)
        var elements: Set<Subject> // 이 요건 아래에 과목이 있다면 과목과의 관계 표현
)