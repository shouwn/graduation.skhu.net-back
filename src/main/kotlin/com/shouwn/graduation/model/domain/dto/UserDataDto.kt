package com.shouwn.graduation.model.domain.dto

import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.entity.Requirement
import com.shouwn.graduation.model.domain.type.RoleName
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserDataDto (

        var userNumber: String? = null,

        val password: String? = null,

        var name: String? = null,

        var email: String? = null,

        var hint: Long? = null,

        var hintAnswer: String? = null,

        var enabled: Boolean? = null,

        var role: RoleName? = null,

        var parties: List<Long>? = null,

        var attends: List<Attend>? = null,

        var requirements: List<Long>? = null
)