package com.shouwn.graduation.model.domain.dto.request

import com.shouwn.graduation.model.domain.type.AttendType
import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType

data class AttendRequest constructor(

        var year: Long,

        var term: TermType,

        var grade: GradeType,

        var name: String,

        var credit: Double,

        var section: SectionType,

        var type: AttendType,

        var courseId: Long
)