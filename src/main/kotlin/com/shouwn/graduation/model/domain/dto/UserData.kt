package com.shouwn.graduation.model.domain.dto

import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.type.RoleName
import org.springframework.data.neo4j.annotation.QueryResult

/*
조회용 DTO 클래스
 */
@QueryResult
data class UserData (
        val id: Long,

        val userNumber: String,

        val name: String,

        val email: String,

        val parties: Set<Party>,

        val credit: Double,

        val role: RoleName
)