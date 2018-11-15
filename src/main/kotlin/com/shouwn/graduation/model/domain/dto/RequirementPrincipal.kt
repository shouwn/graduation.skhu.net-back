package com.shouwn.graduation.model.domain.dto

import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.type.SatisfyingType
import com.shouwn.graduation.model.domain.type.SatisfyingTypeConverter
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.annotation.typeconversion.Convert

class RequirementPrincipal{

    var id: Long? = null

    var name: String? = null // 요건의 이름

    var satisfying: SatisfyingType? = null // 이 요건이 만족되기 위한 조건이 무엇인지 나타냄

    var need: Long? = null // 요건의 타입에 맞춘 요구 값을 나타냄

    var clazzMin: Long? = null

    var clazzMax: Long? = null // 요건이 해당되는 학번을 가르킴 e.g.) 2013 이면 ~ 2013

    var isMeet: Boolean = false

    var party: Party? = null // 요건을 만족해야 하는 소속이 어디인가를 나타냄

    var subs: MutableSet<RequirementPrincipal>? = null // 하위 요건

    var courses: Set<CoursePrincipal>? = null // 이 요건 아래에 과목이 있다면 과목과의 관계 표현
}