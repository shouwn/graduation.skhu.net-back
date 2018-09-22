package com.shouwn.graduation.model.entity

import com.shouwn.graduation.model.domain.SubjectType
import com.shouwn.graduation.model.domain.SubjectTypeConverter
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity
data class Subject constructor(
        @Id @GeneratedValue
        val id: Long,

        @Property
        @Convert(SubjectTypeConverter::class)
        val type: SubjectType,

        @Property
        val content: String,

        @Property
        val value: Int
)