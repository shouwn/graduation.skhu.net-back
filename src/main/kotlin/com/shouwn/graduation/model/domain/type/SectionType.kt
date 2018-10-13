package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

// R: 필수 / S: 선택

enum class SectionType(val value: Int, val labelShort: String, val labelLogn: String){
    LIBERAL_R(0, "교필", "교양필수"),
    LIBERAL_S(1, "교선", "교양선택"),
    MAJOR_R(2, "전필", "전공필수"),
    MAJOR_S(3, "전선", "전공선택"),
    MINOR_R(4, "부필", "부전공필수"),
    MINOR_S(5, "부선", "부전공선택")
    ;

    companion object {
        val map = SectionType.values().associate { it.value to it }

        fun valueOf(value: Int): SectionType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class SectionTypeConverter : AttributeConverter<SectionType, Int> {
    override fun toGraphProperty(value: SectionType?): Int =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Int?): SectionType =
            value?.let { SectionType.valueOf(it) } ?: throw IllegalStateException()
}