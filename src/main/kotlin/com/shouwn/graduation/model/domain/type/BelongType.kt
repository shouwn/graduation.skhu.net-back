package com.shouwn.graduation.model.domain.type

import com.fasterxml.jackson.annotation.JsonFormat
import org.neo4j.ogm.typeconversion.AttributeConverter

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class BelongType(val value: Long, val regex: Regex){
    MAJOR(0, "[^(교양계)].*".toRegex()),
    GENERAL(1, "교양계.*".toRegex())
    ;

    companion object {
        private val map = BelongType.values().associate { it.value to it }

        fun patternOf(value: String) =
                if(MAJOR.regex.matches(value)) MAJOR else GENERAL

        fun valueOf(value: Long): BelongType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

//        @JsonCreator
//        fun getInstance(@JsonProperty("sectionValue") value: Long): BelongType =
//                BelongType.valueOf(value)
    }
}

class BelongTypeConverter : AttributeConverter<BelongType, Long> {
    override fun toGraphProperty(value: BelongType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): BelongType =
            value?.let { BelongType.valueOf(it) } ?: throw IllegalStateException()
}