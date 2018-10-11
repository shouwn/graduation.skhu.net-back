package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class SubjectType constructor(val value: Int){
    CATEGORY(0), // 범주에 충족되는 과목
    SPECIFY(1) // 특정 과목
    ;

    companion object {
        val map = SubjectType.values().associate { it.value to it }

        fun valueOf(value: Int): SubjectType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class SubjectTypeConverter : AttributeConverter<SubjectType, Int>{
    override fun toGraphProperty(value: SubjectType?): Int =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Int?): SubjectType =
            value?.let { SubjectType.valueOf(it) } ?: throw IllegalStateException()
}