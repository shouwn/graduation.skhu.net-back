package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class TermType(val value: Int, val label: String){
    FIRST(0, "1학기"),
    SECOND(1, "2학기"),
    SUMMER(2, "여름학기"),
    WINTER(3, "겨울학기")
    ;

    companion object {
        val map = TermType.values().associate { it.value to it }
        val mapLabel = TermType.values().associate { it.label to it }

        fun valueOf(value: Int): TermType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

        fun labelOf(label: String): TermType =
                mapLabel[label] ?: throw IllegalStateException("$label 에 해당하는 타입이 없습니다.")
    }
}

class TermTypeConverter : AttributeConverter<TermType, Int> {
    override fun toGraphProperty(value: TermType?): Int =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Int?): TermType =
            value?.let { TermType.valueOf(it) } ?: throw IllegalStateException()
}