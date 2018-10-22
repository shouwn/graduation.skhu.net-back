package com.shouwn.graduation.model.domain.dto.request

import com.shouwn.graduation.model.domain.type.BelongType

data class PartyRequest (
        var name: String,

        var belong: BelongType
)