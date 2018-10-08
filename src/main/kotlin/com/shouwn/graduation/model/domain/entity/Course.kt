package com.shouwn.graduation.model.domain.entity

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import java.time.LocalDateTime

@NodeEntity
data class Course constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Property
        var term: LocalDateTime,

        @Property
        var code: String,

        @Property
        var name: String,

        @Property
        var type: String, // 이수구분 enum 예정

        @Property
        var credit: Int, // 학점

        @Property
        var grade: Int // 성적 enum 예정
)