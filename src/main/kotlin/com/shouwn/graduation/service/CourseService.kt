package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.toValueString
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDateTime

@Service
class CourseService @Autowired constructor(
        val courseRepository: CourseRepository
){
    fun addCourseFromFile(user: UserPrincipal, file: InputStream){

        val courseList = arrayListOf<Course.StorageDto>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null) {
                courseList.add(Course.StorageDto(
                        code = row.getCell(2).toValueString(),
                        partyName = row.getCell(3).toValueString().trim().split(" ").last(),
                        name = row.getCell(4).toValueString(),
                        credit = row.getCell(5).numericCellValue,
                        enabled = true
                ).apply { createUserDateAudit(user.id) })
            }
        }

        courseRepository.mergeCourse(courseList)
    }


}