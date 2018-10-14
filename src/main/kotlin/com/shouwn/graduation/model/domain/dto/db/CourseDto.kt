package com.shouwn.graduation.model.domain.dto.db

import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Party
import org.neo4j.ogm.annotation.Index
import org.neo4j.ogm.annotation.Relationship
import java.time.LocalDateTime

data class CourseDto constructor(

        var code: String,

        var name: String,

        var credit: Double, // 학점

        var enabled: Boolean, // 성적 enum 예정

        var party: String,

        var createdBy: Long,

        var updatedBy: Long,

        var createdAt: String = LocalDateTime.now().toString(),

        var updatedAt: String
)