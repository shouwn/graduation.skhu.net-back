package com.shouwn.graduation.model.domain.dto.request

data class CourseRequest (
        var code: String,

        var name: String,

        var credit: Double,

        var enabled: Boolean,

        var partyId: Long
)