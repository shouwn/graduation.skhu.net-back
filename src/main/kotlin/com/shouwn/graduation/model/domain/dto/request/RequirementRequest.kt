package com.shouwn.graduation.model.domain.dto.request

import com.shouwn.graduation.model.domain.type.SatisfyingType

data class RequirementRequest (
        val name: String,

        val satisfyingType: SatisfyingType,

        val need: Long,

        val clazzMin: Long?,

        val clazzMax: Long?,

        val party: Long?,

        val target: Set<Long>
)