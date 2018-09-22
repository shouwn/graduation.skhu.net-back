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
        val name: String,

        @Convert(SatisfyingTypeConverter::class)
        val satisfying: SatisfyingType,

        @Property
        val require: Int,

        @Relationship(type = "child", direction = Relationship.OUTGOING)
        val children: Set<Requirement>,

        @Relationship(type = "element", direction = Relationship.OUTGOING)
        val elements: Set<Requirement>
)