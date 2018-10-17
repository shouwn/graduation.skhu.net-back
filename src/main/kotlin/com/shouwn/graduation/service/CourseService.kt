package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.value
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

        val courseList = arrayListOf<Course.StorageValue>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null) {
                courseList.add(Course.StorageValue(
                        code = row.getCell(2).value(),
                        partyName = row.getCell(3).value().trim().split(" ").last(),
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