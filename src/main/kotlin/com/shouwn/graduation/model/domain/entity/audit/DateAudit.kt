package com.shouwn.graduation.model.domain.entity.audit

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.neo4j.ogm.annotation.Property
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.LocalDateTime

abstract class DateAudit{
    var createdAt: LocalDateTime? = null

    var updatedAt: LocalDateTime? = null

    fun createDateAudit(){
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    fun updateDateAudit(){
        this.updatedAt = LocalDateTime.now()
    }

    fun updateDateAudit(before: DateAudit){
        this.createdAt = before.createdAt
        this.updatedAt = LocalDateTime.now()
    }
}