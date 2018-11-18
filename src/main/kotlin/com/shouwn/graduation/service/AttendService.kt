package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.AttendDataDto
import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.AttendType
import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType
import com.shouwn.graduation.repository.AttendRepository
import com.shouwn.graduation.repository.CourseRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.logger
import com.shouwn.graduation.utils.toValueString
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.LocalDateTime
import kotlin.reflect.full.declaredMembers

@Service
class AttendService @Autowired constructor(
        private val attendRepository: AttendRepository,
        private val partyService: PartyService,
        private val courseRepository: CourseRepository
){
    private val logger = logger()

    @Transactional
    fun addAttendFromFile(user: UserPrincipal, file: InputStream): List<Attend>{

        attendRepository.deleteAllByUserIdAndType(user.id, AttendType.DONE)

        val attendList = arrayListOf<Attend>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        // TODO(해당 과목이 없을 경우 체크)

        for(rowIndex in 2..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null){
                attendList.add(Attend(
                        year = row.getCell(1).numericCellValue.toLong(),
                        term = TermType.labelOf(row.getCell(2).toValueString()),
                        name = row.getCell(4).toValueString(),
                        section = SectionType.valueOfLabelShort(row.getCell(5).toValueString()),
                        grade = GradeType.labelOf(row.getCell(7).toValueString()),
                        credit = row.getCell(6).numericCellValue,
                        type = AttendType.DONE
                ).apply {
                    createUserDateAudit(user.id)
                    course = Course(code = row.getCell(3).toValueString())
                })
            }
        }

        return attendRepository.addAttend(user.id, attendList)
    }

    @Transactional
    fun saveAttends(user: User, requests: List<AttendDataDto>) {
        if(requests.isEmpty())
            throw ApiException(
                    status = HttpStatus.BAD_REQUEST,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "보내진 데이터가 없습니다."
                    )
            )

        val attendType = requests.first().apply {
            requests.forEach {
                if(this.type != it.type)
                    throw ApiException(
                            status = HttpStatus.PRECONDITION_FAILED,
                            apiResponse = ApiResponse(
                                    success = false,
                                    message = "같은 타입의 attend 끼리만 저장 가능합니다."
                            )
                    )
            }
        }.type

        val partyMap = partyService.findPartiesByIds(
                requests.asSequence().map {
                    it.courseId
                            ?: throw ApiException(
                                    status = HttpStatus.PRECONDITION_FAILED,
                                    apiResponse = ApiResponse(
                                            success = false,
                                            message = "과목은 항상 필요합니다."
                                    )
                            )
                }.toList()).associate { it.id to it }

        attendRepository.deleteAllByUserIdAndType(user.id!!, attendType!!)

        attendRepository.saveAll(
                requests.map {
                    Attend(
                            year = it.year!!,
                            name = it.name!!,
                            term = it.term!!,
                            grade = it.grade!!,
                            credit = it.credit!!,
                            section = it.section!!,
                            type = it.type!!
                    ).apply {
                        this.user = user
                        this.course = courseRepository.findById(it.courseId!!)
                                .orElseThrow {
                                    ApiException(
                                            status = HttpStatus.PRECONDITION_FAILED,
                                            apiResponse = ApiResponse(
                                                    success = false,
                                                    message = "${it.courseId}에 해당하는 과목이 없습니다."
                                            )
                                    )
                                }
                    }
                }
        )
    }

    fun modifyAttend(user: User, request: AttendDataDto, attendId: Long) =
            this.findAttendsByIds(listOf(attendId)).first()
                    .let {
                        it.copy(
                                year = request.year ?: it.year,
                                name = request.name ?: it.name,
                                term = request.term ?: it.term,
                                grade = request.grade ?: it.grade,
                                credit = request.credit ?: it.credit,
                                section = request.section ?: it.section
                                // type 은 수강 예정이냐, 수강한 것이냐를 가르키기 때문에
                                // 변경 되면 안 되어서 제외
                        ).apply {
                            this.updateUserDateAudit(user.id!!, it)
                            this.user = user
                            if (it.course?.id != request.courseId) {
                                println(it.course?.id)
                                println(request.courseId)
                                this.course =
                                        findAllById(courseRepository, listOf(request.courseId!!)).first()
                                this.name = "${this.name} -> ${this.course!!.name}"
                            }
                            else
                                this.course = it.course
                        }
                    }.let { attendRepository.updateAttend(
                            attendId = it.id!!,
                            courseId = it.course!!.id!!,
                            year = it.year,
                            name = it.name,
                            credit = it.credit,
                            term = it.term.value,
                            grade = it.grade.value,
                            section = it.section.value,
                            type = it.type.value,
                            updatedAt = it.updatedAt.toString(),
                            updatedBy = it.updatedBy!!
                    ) }

    fun findAttendsByIds(ids: Iterable<Long>) =
            findAllById(attendRepository, ids)

    fun attendsByUserId(userId: Long) =
            this.attendRepository.findAllByUserIdOrderByYearAndTerm(userId)
}