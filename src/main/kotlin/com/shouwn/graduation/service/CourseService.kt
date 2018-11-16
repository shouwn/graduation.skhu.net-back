package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.CourseRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.repository.PartyRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.info
import com.shouwn.graduation.utils.logger
import com.shouwn.graduation.utils.toValueString
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.LocalDateTime

@Service
class CourseService @Autowired constructor(
        private val courseRepository: CourseRepository,
        private val partyRepository: PartyRepository
){
    private val logger = logger()

    fun findCourseCanReplaced(courseId: Long) =
            courseRepository.findAllReplacedCourse(courseId)

    fun disableCourse(user: User, courseId: Long) =
            courseRepository.findById(courseId).orElseThrow { throw ApiException(
                    status = HttpStatus.NOT_FOUND,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "해당 과목을 찾을 수 없습니다."
                    )
            ) }.let {
                it.copy(
                        enabled = false
                ).apply {
                    updateUserDateAudit(user.id!!, it)
                    replacement = it.replacement
                    parties = it.parties
                }
            }.let { courseRepository.save(it) }

    fun setReplaceByFile(user: User, file: InputStream){ // 문제 소지 있음. 폐강되었다가 다시 부활하면? 대체가 풀리면?
        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        val courseMap = courseRepository.findAll().associate { it.code to it!! }

        val changedCourses = mutableListOf<Course>()

        for(rowIndex in 1..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null){
                val code = row.getCell(2).toValueString().trim()
                if(courseMap[code] != null) {
                    courseMap[code]!!.enabled = false
                    val replaceCode = row.getCell(6).toValueString().trim()

                    if (replaceCode.isNotEmpty() && courseMap[replaceCode] != null) {
                        courseMap[code]!!.replacement = courseMap[replaceCode]!!
                    }

                    courseMap[code]?.let { changedCourses.add(it) }
                }
            }
        }

        courseRepository.saveAll(changedCourses)
    }

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
                        name = row.getCell(4).toValueString(),
                        enabled = true
                ).apply {
                    createUserDateAudit(user.id)
                    parties = setOf(Party(id = partyId))
                })
            }
        }

        return courseRepository.mergeCourse(courseList)
                .apply { logger.info("${user.info()} 가 과목 데이터를 파일을 이용하여 저장함") }
    }

    //    @Transactional // 트랜젝션 거니까 return 할 때 이전에 있던 데이터 가져오는 문제가 생김 버그인 것 같음
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
                .let {
                    it.copy(
                            name = request.name,
                            enabled = request.enabled
                    ).apply { updateUserDateAudit(user.id, it) }
                }

        if(course.parties!!.asSequence().map { it.id }.toSet() == request.partyIds)
            return courseRepository.update(
                    courseId = course.id!!,
                    courseName = course.name!!,
                    courseEnabled = course.enabled!!,
                    updatedAt = course.updatedAt.toString(),
                    updatedBy = course.updatedBy!!
            ).apply { logger.info("${user.info()} 가 ${course.id} 과목을 수정함") }

        val party = partyRepository.findAllById(request.partyIds).associate { it.id to it }

        for(partyId in request.partyIds)
            if(!party.containsKey(partyId))
                throw ApiException(
                        status = HttpStatus.PRECONDITION_FAILED,
                        apiResponse = ApiResponse(
                                success = false,
                                message = "아이디가 ${partyId}인 소속이 없습니다."
                        )
                ).apply { logger.error("${user.info()} 가 존재하지 않는 $partyId 소속으로 ${courseId}를 변경하려고 시도") }

        return courseRepository.update(
                partyIds = request.partyIds,
                courseId = course.id!!,
                courseName = course.name!!,
                courseEnabled = course.enabled!!,
                updatedAt = course.updatedAt.toString(),
                updatedBy = course.updatedBy!!
        ).apply { logger.info("${user.info()} 가 ${course.id} 과목을 수정함") }
    }

    fun findCoursesByIds(ids: Iterable<Long>) =
            findAllById(courseRepository, ids).toSet()

    fun findCoursesLikeCodeAndName(code: String, name: String, partyName: String) =
            courseRepository.findAllLikeCodeAndName(
                    code.let { ".*$it.*" },
                    name.let { ".*$it.*" },
                    partyName.let { ".*$it.*" }
            )
}