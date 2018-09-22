package com.shouwn.graduation.model.type

import org.neo4j.ogm.typeconversion.AttributeConverter
import org.neo4j.ogm.typeconversion.CompositeAttributeConverter

data class Satisfying constructor(
        val satisfyingType: SatisfyingType,
        val target: Int
)

enum class SatisfyingType(val value: Int){
    SUBJECT_ALL(0), // 하위 과목 모두 수강
    SUBJECT_OVER(1), // 하위 과목 몇 점 이상 수강
    SUBJECT_COUNT(2) // 하위 과목 몇 개 이상 수강
    ;

    companion object {
        val map = SatisfyingType.values().associate { it.value to it }

        fun valueOf(value: Int): SatisfyingType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class SatisfyingConverter : CompositeAttributeConverter<Satisfying> {
    override fun toGraphProperties(value: Satisfying?): MutableMap<String, *> =
            value?.let {
                mutableMapOf("satisfying" to it.satisfyingType, "target" to it.target)
            } ?: throw IllegalStateException()

    override fun toEntityAttribute(value: MutableMap<String, *>?): Satisfying =
            value?.let {
                it.map { Satisfying(SatisfyingType.valueOf(value["satisfyingType"] as Int), value["target"] as Int) }.first()
            } ?: throw IllegalStateException()
}