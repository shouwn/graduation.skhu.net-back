package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.type.TermType
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.security.UserPrincipal
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException

@Service
class CourseService @Autowired constructor(
        val courseRepository: CourseRepository
){
    fun addCourseFromFile(user: UserPrincipal, file: InputStream){

        val courseList = arrayListOf<Course>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null) {
                courseList.add(Course(
                        term = TermType.labelOf(row.getCell(1).value()),
                        code = row.getCell(2).value(),
                        party = Party(name = row.getCell(3).value()),
                        name = row.getCell(4).value(),
                        credit = row.getCell(5).numericCellValue,
                        enabled = true
                ))
            }
        }

        courseRepository.mergeCourse(courseList)
    }

    fun Cell.value(): String =
            when(this.cellType){
                Cell.CELL_TYPE_NUMERIC -> this.numericCellValue.toString()
                Cell.CELL_TYPE_STRING -> this.stringCellValue
                Cell.CELL_TYPE_BOOLEAN -> this.booleanCellValue.toString()
                Cell.CELL_TYPE_ERROR -> this.errorCellValue.toString()
                else -> throw IllegalArgumentException()
            }
}