package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.db.CourseDto
import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.type.TermType
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.value
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@Service
class CourseService @Autowired constructor(
        val courseRepository: CourseRepository
){
    fun addCourseFromFile(user: UserPrincipal, file: InputStream){

        val courseList = arrayListOf<CourseDto>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null) {
                courseList.add(CourseDto(
                        code = row.getCell(2).value(),
                        party = row.getCell(3).value(),
                        name = row.getCell(4).value(),
                        credit = row.getCell(5).numericCellValue,
                        enabled = true,
                        createdBy = user.id,
                        updatedBy = user.id,
                        updatedAt = LocalDateTime.now().toString()
                ))
            }
        }

        courseRepository.mergeCourse(courseList)
    }


}