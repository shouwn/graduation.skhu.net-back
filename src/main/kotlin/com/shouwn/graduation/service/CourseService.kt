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
        private val courseRepository: CourseRepository,
        private val partyRepository: PartyRepository
){
    private val logger = logger()

    fun addCourseFromFile(user: UserPrincipal, file: InputStream): List<Course>{

        val courseList = arrayListOf<Course>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        val partyMap = partyRepository.findAll().associate { it.name to it }

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null) {
                val partyName = row.getCell(3).toValueString().trim()

                val partyId =
                        if (partyMap.containsKey(partyName))
                            partyMap[partyName]!!.id
                        else
                            throw ApiException(
                                    status = HttpStatus.PRECONDITION_FAILED,
                                    apiResponse = ApiResponse(
                                            success = false,
                                            message = "${partyName}에 해당하는 개설 소속이 없습니다."
                                    )
                            )

                courseList.add(Course(
                        code = row.getCell(2).toValueString(),
                        party = Party(id = partyId),
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
                .copy(
                        code = request.code,
                        name = request.name,
                        credit = request.credit,
                        enabled = request.enabled
                )

        if(course.party?.id == request.partyId)
            return courseRepository.save(course)
                    .apply { logger.info("${user.info()} 가 ${course.id} 과목을 수정함") }

        println(course.party?.id)

        val party = partyRepository.findById(request.partyId)

        if(!party.isPresent)
            throw ApiException(
                    status = HttpStatus.PRECONDITION_FAILED,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "아이디가 ${request.partyId}인 소속이 없습니다."
                    )
            ).apply { logger.error("${user.info()} 가 존재하지 않는 ${request.partyId} 소속으로 ${courseId}를 변경하려고 시도") }

        return courseRepository.updateCourse(request.partyId, course)
                .apply { logger.info("${user.info()} 가 ${course.id} 과목을 수정함") }
    }

}