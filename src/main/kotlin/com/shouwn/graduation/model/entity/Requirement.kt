package com.shouwn.graduation.model.entity

import com.shouwn.graduation.model.type.Satisfying
import com.shouwn.graduation.model.type.SatisfyingConverter
import com.shouwn.graduation.model.type.SatisfyingType
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Requirement constructor(
        @Id @GeneratedValue
        val id: Long,

        @Property
        val name: String,

        @Convert(SatisfyingConverter::class)
        val satisfying: Satisfying
)