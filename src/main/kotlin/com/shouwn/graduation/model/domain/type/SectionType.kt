package com.shouwn.graduation.model.domain.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.neo4j.ogm.typeconversion.AttributeConverter

// R: 필수 / S: 선택

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SectionType(val value: Long, val labelShort: String, val labelLong: String){
    LIBERAL_R(0, "교필", "교양필수"),
    LIBERAL_S(1, "교선", "교양선택"),
    MAJOR_R(2, "전필", "전공필수"),
    MAJOR_S(3, "전선", "전공선택"),
    MINOR_R(4, "부필", "부전공필수"),
    MINOR_S(5, "부선", "부전공선택")
    ;

    companion object {
        private val map = SectionType.values().associate { it.value to it }
        private val mapLabelShort = SectionType.values().associate { it.labelShort to it }
        private val mapLabelLong = SectionType.values().associate { it.labelLong to it }

        fun valueOf(value: Long): SectionType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

        fun valueOfLabelShort(value: String): SectionType =
                mapLabelShort[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

        fun valueOfLabelLong(value: String): SectionType =
                mapLabelLong[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

//        @JsonCreator
//        fun getInstance(@JsonProperty("sectionValue") value: Long): SectionType =
//                SectionType.valueOf(value)
    }
}

class SectionTypeConverter : AttributeConverter<SectionType, Long> {
    override fun toGraphProperty(value: SectionType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): SectionType =
            value?.let { SectionType.valueOf(it) } ?: throw IllegalStateException()
}