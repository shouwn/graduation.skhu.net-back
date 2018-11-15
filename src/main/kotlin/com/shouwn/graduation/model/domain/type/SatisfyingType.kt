package com.shouwn.graduation.model.domain.type

import com.fasterxml.jackson.annotation.JsonFormat
import org.neo4j.ogm.typeconversion.AttributeConverter

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SatisfyingType(val value: Long, val label: String){
    COURSE_CREDIT(0, "필수 과목 학점"), // 하위 과목 몇 점 이상 수강
    COURSE_COUNT(1, "필수 과목 갯수"), // 하위 과목 몇 개 이상 수강 (중복 가능)
    CHILDREN(2, "하위 요건"), // 하위 자식 요건들 충족
    GENERAL(3, "교양"), // 교양
    MAJOR(4, "전공"), // 전공
    MINOR(5, "부전공"), // 부전공
    ALL(6, "총학점") // 총 학점
    ;

    companion object {
        private val map = SatisfyingType.values().associate { it.value to it }

        val COURSE_SET = setOf(COURSE_COUNT, COURSE_CREDIT)

        fun valueOf(value: Long): SatisfyingType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

//        @JsonCreator
//        fun getInstance(@JsonProperty("satisfyingValue") value: Long): SatisfyingType =
//                SatisfyingType.valueOf(value)
    }
}

class SatisfyingTypeConverter : AttributeConverter<SatisfyingType, Long> {
    override fun toGraphProperty(value: SatisfyingType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): SatisfyingType =
            value?.let { SatisfyingType.valueOf(it) } ?: throw IllegalStateException()
}