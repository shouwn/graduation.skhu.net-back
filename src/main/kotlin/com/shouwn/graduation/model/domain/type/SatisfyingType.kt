package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class SatisfyingType(val value: Long){
    SUBJECT_ALL(0), // 하위 과목 모두 수강
    SUBJECT_OVER(1), // 하위 과목 몇 점 이상 수강
    CHILDREN_ALL(2), // 하위 자식 요건들 모두 충족
    CHILDREN_OVER(3), // 하위 자식 요건들 몇 개 이상 충족
    ;

    companion object {
        private val map = SatisfyingType.values().associate { it.value to it }

        fun valueOf(value: Long): SatisfyingType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class SatisfyingTypeConverter : AttributeConverter<SatisfyingType, Long> {
    override fun toGraphProperty(value: SatisfyingType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): SatisfyingType =
            value?.let { SatisfyingType.valueOf(it) } ?: throw IllegalStateException()
}