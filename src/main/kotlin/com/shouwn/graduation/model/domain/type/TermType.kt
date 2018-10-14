package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class TermType(val value: Long, val label: String){
    FIRST(0, "1학기"),
    SECOND(1, "2학기"),
    SUMMER(2, "여름학기"),
    WINTER(3, "겨울학기"),
    GRAD_FIRST(4, "1학기(메인즈)"),
    GRAD_SECOND(5, "2학기(메인즈)"),
    GRAD_THIRD(6, "3학기(메인즈)"),
    GRAD_FOURTH(7, "4학기(메인즈)")
    ;

    companion object {
        private val map = TermType.values().associate { it.value to it }
        private val mapLabel = TermType.values().associate { it.label to it }

        fun valueOf(value: Long): TermType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

        fun labelOf(label: String): TermType =
                mapLabel[label] ?: throw IllegalStateException("$label 에 해당하는 타입이 없습니다.")
    }
}

class TermTypeConverter : AttributeConverter<TermType, Long> {
    override fun toGraphProperty(value: TermType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): TermType =
            value?.let { TermType.valueOf(it) } ?: throw IllegalStateException()
}