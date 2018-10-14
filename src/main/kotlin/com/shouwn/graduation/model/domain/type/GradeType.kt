package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class GradeType(val value: Long, val grade: Double, val label: String){
    AP(0,4.5, "A+"),
    A(1,4.0, "A0"),
    BP(2,3.5, "B+"),
    B(3,3.0, "B0"),
    CP(4,2.5, "C+"),
    C(5,2.0, "C0"),
    DP(6,1.5, "D+"),
    D(7,1.0, "D0"),
    F(8,0.0, "F"),
    P(9, 0.0, "P"),
    N(10, 0.0, "N")
    ;

    companion object {
        private val map = GradeType.values().associate { it.value to it }
        private val mapLabel = GradeType.values().associate { it.label to it }

        fun valueOf(value: Long): GradeType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")

        fun labelOf(value: String): GradeType =
                mapLabel[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class GradeTypeConverter : AttributeConverter<GradeType, Long> {
    override fun toGraphProperty(value: GradeType?): Long =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Long?): GradeType =
            value?.let { GradeType.valueOf(it) } ?: throw IllegalStateException()
}