package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.model.domain.type.RoleNameConverter
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.springframework.data.neo4j.annotation.QueryResult

/*
조회용 DTO 클래스
 */
@QueryResult
class UserData{
    var id: Long = 0

    lateinit var userNumber: String

    lateinit var name: String

    lateinit var email: String

    lateinit var parties: List<Party>

    var credit: Double = 0.0

    @Convert(RoleNameConverter::class)
    lateinit var role: RoleName
}