package com.shouwn.graduation.model.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.shouwn.graduation.model.domain.entity.audit.DateAudit
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.model.domain.type.RoleNameConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

@NodeEntity(label = "User")
data class User constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Index(unique = true)
        var userNumber: String, // 교직원 혹은 학생 번호

        @JsonIgnore
        var password: String, // 비밀번호

        @Index
        var name: String, // 사용자 이름

        var email: String, // 사용자 비밀번호

        var hint: Long, // 사용자 비밀번호 찾기 힌트

        var hintAnswer: String, // 사용자 비밀번호 찾기 힌트에 대한 답변

        @JsonIgnore
        var enabled: Boolean, // 사용자가 승인 되었는지 나타내는 속성

        @Convert(RoleNameConverter::class)
        var role: RoleName // 사용자의 권한

) : DateAudit() {

    @JsonIgnore
    @Relationship(type = "ATTEND", direction = Relationship.OUTGOING)
    var attends: List<Attend>? = null

    @JsonIgnore
    @Relationship(type = "SELECT", direction = Relationship.OUTGOING)
    var requirement: Requirement? = null

    @Relationship(type = "BELONG", direction = Relationship.OUTGOING)
    var parties: List<Party>? = null // IT 융합부에 소속되면서 소프트웨어공학과에 소속된다면?
}