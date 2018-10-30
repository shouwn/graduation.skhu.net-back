package com.shouwn.graduation.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class YearService{
    @Value("\${year.first}")
    private var firstYear: Int? = null

    @Value("\${year.second}")
    private var secondYear: Int? = null

    @Value("\${year.third}")
    private var thirdYear: Int? = null

    @Value("\${year.fourth}")
    private var fourthYear: Int? = null

    fun getYear(credit: Double): Int  =
            when {
                credit < firstYear!! -> 1
                credit < secondYear!! -> 2
                credit < thirdYear!! -> 3
                else -> 4
            }

    fun getCompletionCredit(year: Int) =
            when (year) {
                1 -> firstYear!!
                2 -> secondYear!!
                3 -> thirdYear!!
                4 -> fourthYear!!
                else -> throw IllegalArgumentException("$year 학년은 없습니다.")
            }
}