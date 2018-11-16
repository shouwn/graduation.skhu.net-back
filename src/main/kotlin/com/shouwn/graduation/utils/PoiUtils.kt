package com.shouwn.graduation.utils

import org.apache.poi.ss.usermodel.Cell
import java.lang.IllegalArgumentException

fun Cell.toValueString(): String =
        when(this.cellType){
            Cell.CELL_TYPE_NUMERIC -> this.numericCellValue.toString()
            Cell.CELL_TYPE_STRING -> this.stringCellValue
            Cell.CELL_TYPE_BOOLEAN -> this.booleanCellValue.toString()
            Cell.CELL_TYPE_ERROR -> this.errorCellValue.toString()
            Cell.CELL_TYPE_BLANK -> ""
            else -> throw IllegalArgumentException()
        }