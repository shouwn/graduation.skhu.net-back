package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class GradeType(val value: Int, val grade: Double, val label: String){
    AP(0,4.5, "A+"),
    A(1,4.0, "A"),
    BP(2,3.5, "B+"),
    B(3,3.0, "B"),
    CP(4,2.5, "C+"),
    C(5,2.0, "C"),
    DP(6,1.5, "D+"),
    D(7,1.0, "D"),
    F(8,0.0, "F"),
    P(9, 0.0, "P"),
    N(10, 0.0, "N")
    ;

    companion object {
        val map = GradeType.values().associate { it.value to it }

        fun valueOf(value: Int): GradeType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class GradeTypeConverter : AttributeConverter<GradeType, Int> {
    override fun toGraphProperty(value: GradeType?): Int =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Int?): GradeType =
            value?.let { GradeType.valueOf(it) } ?: throw IllegalStateException()
}