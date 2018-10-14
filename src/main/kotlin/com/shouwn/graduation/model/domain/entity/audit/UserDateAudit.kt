package com.shouwn.graduation.model.domain.entity.audit

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@JsonIgnoreProperties(
        value = ["createdBy", "updatedBy"],
        allowGetters = true
)
abstract class UserDateAudit : DateAudit() {
    var createdBy: Long? = null

    @LastModifiedBy
    var updatedBy: Long? = null
}