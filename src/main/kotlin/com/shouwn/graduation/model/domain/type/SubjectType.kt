package com.shouwn.graduation.model.domain.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.neo4j.ogm.typeconversion.AttributeConverter

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SubjectType constructor(val value: Long){
    CATEGORY(0), // 범주에 충족되는 과목
    SPECIFY(1) // 특정 과목
    ;

    companion object {
        private val map = SubjectType.values().associate { it.value to it }

        fun valueOf(value: Long): SubjectType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

//        @JsonCreator
//        fun getInstance(@JsonProperty("subjectValue") value: Long): SubjectType =
//                SubjectType.valueOf(value)
    }
}

class SubjectTypeConverter : AttributeConverter<SubjectType, Long>{
    override fun toGraphProperty(value: SubjectType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): SubjectType =
            value?.let { SubjectType.valueOf(it) } ?: throw IllegalStateException()
}