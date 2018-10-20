package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.CourseRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.repository.PartyRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.info
import com.shouwn.graduation.utils.logger
import com.shouwn.graduation.utils.toValueString
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.LocalDateTime

@Service
class CourseService @Autowired constructor(
        val courseRepository: CourseRepository,
        val partyRepository: PartyRepository
){
    private val logger = logger()

    fun addCourseFromFile(user: UserPrincipal, file: InputStream): List<Course>{

        val courseList = arrayListOf<Course>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null) {
                courseList.add(Course(
                        code = row.getCell(2).toValueString(),
                        party = Party(name = row.getCell(3).toValueString().trim()),
                        name = row.getCell(4).toValueString(),
                        credit = row.getCell(5).numericCellValue,
                        enabled = true
                ).apply { createUserDateAudit(user.id) })
            }
        }

        return courseRepository.mergeCourse(courseList)
                .apply { logger.info("${user.info()} 가 과목 데이터를 파일을 이용하여 저장함") }
    }

    @Transactional
    fun updateCourse(user: UserPrincipal, courseId: Long, request: CourseRequest): Course {
        val optionalCourse = courseRepository.findById(courseId)

        if(!optionalCourse.isPresent)
            throw ApiException(
                    status = HttpStatus.NOT_FOUND,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "아이디가 ${courseId}인 과목이 없습니다."
                    )
            ).apply { logger.error("${user.info()} 가 존재하지 않는 과목을 수정하려고 시도함") }

        val course = optionalCourse.get()

        if(course.party.id == request.partyId)
            return courseRepository.save(course.copy(
                    code = request.code,
                    name = request.name,
                    credit = request.credit,
                    enabled = request.enabled
            )).apply { logger.info("${user.info()} 가 ") }

        val party = partyRepository.findById(request.partyId)

        if(!party.isPresent)
            throw ApiException(
                    status = HttpStatus.PRECONDITION_FAILED,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "아이디가 ${request.partyId}인 소속이 없습니다."
                    )
            ).apply { logger.error("${user.info()} 가 존재하지 않는 ${request.partyId} 소속으로 ${courseId}를 변경하려고 시도") }


    }

}