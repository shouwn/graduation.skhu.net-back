package com.shouwn.graduation.model.domain.type

import com.fasterxml.jackson.annotation.JsonFormat
import org.neo4j.ogm.typeconversion.AttributeConverter


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class AttendType(val value: Long){
    DONE(0),
    PROMISE(1)
    ;

    companion object {
        private val map = AttendType.values().associate { it.value to it }

        fun valueOf(value: Long): AttendType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

//        @JsonCreator
//        fun getInstance(@JsonProperty("sectionValue") value: Long): AttendType =
//                AttendType.valueOf(value)
    }
}

class AttendTypeConverter : AttributeConverter<AttendType, Long> {
    override fun toGraphProperty(value: AttendType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): AttendType =
            value?.let { AttendType.valueOf(it) } ?: throw IllegalStateException()
}