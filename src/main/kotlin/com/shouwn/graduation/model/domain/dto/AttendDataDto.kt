package com.shouwn.graduation.model.domain.dto

import com.shouwn.graduation.model.domain.type.AttendType
import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType

data class AttendDataDto (

        var year: Long? = null,

        var name: String? = null,

        var term: TermType? = null,

        var grade: GradeType? = null,

        var credit: Double? = null,

        var section: SectionType? = null,

        var type: AttendType? = null,

        var courseId: Long? = null
)