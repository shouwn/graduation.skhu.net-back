package com.shouwn.graduation.model.domain.dto.request

import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType

data class AttendRequest constructor(
        var id: Long? = null,

        var year: Int,

        var term: TermType,

        var grade: GradeType,

        var section: SectionType,

        var courseId: Long
)