package com.shouwn.graduation.model.domain

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Hint(val value: Long, val label: String){
    H1(0, "가장 소중한 보물"),
    H2(1, "어머니 이름"),
    H3(2, "아버지 이름"),
    H4(3, "태어난 장소")
}