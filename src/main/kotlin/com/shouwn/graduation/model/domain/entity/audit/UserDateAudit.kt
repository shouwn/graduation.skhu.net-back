package com.shouwn.graduation.model.domain.entity.audit

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

abstract class UserDateAudit : DateAudit() {
    var createdBy: Long? = null

    var updatedBy: Long? = null

    fun createUserDateAudit(userId: Long){
        super.createDateAudit()
        createdBy = userId
        updatedBy = userId
    }

    fun updateUserDateAudit(userId: Long){
        super.updateDateAudit()
        this.updatedBy = userId
    }

    fun updateUserDateAudit(userId: Long, before: UserDateAudit){
        super.updateDateAudit(before)
        this.createdBy = before.createdBy
        this.updatedBy = userId
    }
}