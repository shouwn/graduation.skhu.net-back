package com.shouwn.graduation.model.entity

import com.shouwn.graduation.model.domain.SatisfyingType
import com.shouwn.graduation.model.domain.SatisfyingTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Requirement constructor(
        @Id @GeneratedValue
        val id: Long,

        @Property
        val name: String, // 요건의 이름

        @Convert(SatisfyingTypeConverter::class)
        val satisfying: SatisfyingType, // 이 요건이 만족되기 위한 조건이 무엇인지 나타냄

        @Property
        val require: Int, // 요건의 타입에 맞춘 요구 값을 나타냄

        @Relationship(type = "child", direction = Relationship.OUTGOING)
        val children: Set<Requirement>, // 하위 요건

        @Relationship(type = "element", direction = Relationship.OUTGOING)
        val elements: Set<Subject> // 이 요건 아래에 과목이 있다면 과목과의 관계 표현
)