package com.shouwn.graduation.model.domain.dto

import com.shouwn.graduation.model.domain.entity.Party
import org.neo4j.ogm.annotation.*

class CoursePrincipal{

        var id: Long? = null

        var code: String? = null // 강의 코드

        var name: String? = null // 강의 이름

        var enabled: Boolean? = null // 폐강인지 아닌지 표현

        var isMeet: Boolean = false

        var replacement: CoursePrincipal? = null // 대체 과목을 표현

        var parties: Set<Party>? = null // 어떤 소속에서 개설 되었는지 표현
}