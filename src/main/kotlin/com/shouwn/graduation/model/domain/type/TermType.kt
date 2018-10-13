package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class TermType(val value: Int){
    FIRST(0),
    SECOND(1),
    SUMMER(2),
    WINTER(3)
    ;

    companion object {
        val map = TermType.values().associate { it.value to it }

        fun valueOf(value: Int): TermType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class TermTypeConverter : AttributeConverter<TermType, Int> {
    override fun toGraphProperty(value: TermType?): Int =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Int?): TermType =
            value?.let { TermType.valueOf(it) } ?: throw IllegalStateException()
}