package com.shouwn.graduation.model.domain.dto.request

import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType

data class AttendRequest constructor(

        var year: Int,

        var term: Long,

        var grade: Long,

        var section: Long,

        var courseCode: String
)