package com.shouwn.graduation.model.domain.type

import com.fasterxml.jackson.annotation.JsonFormat
import org.neo4j.ogm.typeconversion.AttributeConverter

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SearchType(val value: Long, val label: String){
    ALL(0, "전체"),
    NAME(1, "이름"),
    NUMBER(2, "번호")
    ;

    companion object {
        private val map = SearchType.values().associate { it.value to it }

        fun valueOf(value: Long): SearchType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

//        @JsonCreator
//        fun getInstance(@JsonProperty("satisfyingValue") value: Long): SearchType =
//                SearchType.valueOf(value)
    }
}

class SearchTypeConverter : AttributeConverter<SearchType, Long> {
    override fun toGraphProperty(value: SearchType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): SearchType =
            value?.let { SearchType.valueOf(it) } ?: throw IllegalStateException()
}