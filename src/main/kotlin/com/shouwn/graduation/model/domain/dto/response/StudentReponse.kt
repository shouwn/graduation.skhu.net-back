package com.shouwn.graduation.model.domain.dto.response

import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.type.RoleName

data class StudentReponse (

        val id: Long,

        val userNumber: String,

        val year: Long,

        val name: String,

        val email: String,

        val role: RoleName,

        val parties: Set<Party>
)