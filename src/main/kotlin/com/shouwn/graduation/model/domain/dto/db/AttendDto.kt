package com.shouwn.graduation.model.domain.dto.db

import java.time.LocalDateTime

data class AttendDto constructor(

        var year: Int,

        var term: Long,

        var grade: Long,

        var userNumber: String,

        var courseCode: String,

        var section: Long,

        var createdBy: Long,

        var updatedBy: Long,

        var createdAt: String = LocalDateTime.now().toString(),

        var updatedAt: String
)