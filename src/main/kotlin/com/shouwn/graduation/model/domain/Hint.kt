package com.shouwn.graduation.model.domain

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Hint(val value: Long, val label: String){
    H1(0, "가장 기억에 남는 장소는?"),
    H2(1, "나의 보물 제1호는?"),
    H3(2, "나의 출신 초등학교는?"),
    H4(3, "좋아하는 스포츠 팀 이름은?")
}