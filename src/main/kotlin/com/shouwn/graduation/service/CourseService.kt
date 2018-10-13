package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.type.TermType
import com.shouwn.graduation.repository.CourseRepository
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.lang.IllegalArgumentException
import java.nio.file.attribute.UserPrincipal

@Service
class CourseService @Autowired constructor(
        val courseRepository: CourseRepository
){
    fun addCourseFromFile(user: UserPrincipal, file: FileInputStream){

        val courseList = arrayListOf<Course>()

        val sheet = HSSFWorkbook(file).getSheetAt(0)
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
}

fun HSSFCell.value(): String =
        when(this.cellTypeEnum){
            CellType.NUMERIC -> this.numericCellValue.toString()
            CellType.STRING -> this.stringCellValue
            CellType.BOOLEAN -> this.booleanCellValue.toString()
            CellType.ERROR -> this.errorCellValue.toString()
            else -> throw IllegalArgumentException()
        }