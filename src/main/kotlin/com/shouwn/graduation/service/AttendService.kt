package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType
import com.shouwn.graduation.repository.AttendRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.logger
import com.shouwn.graduation.utils.toValueString
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDateTime

@Service
class AttendService @Autowired constructor(
        val attendRepository: AttendRepository
){
    private val logger = logger()

    fun addAttendFromFile(user: UserPrincipal, file: InputStream): List<Attend>{

        val attendList = arrayListOf<Attend>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        // TODO(해당 과목이 없을 경우 체크)

        for(rowIndex in 2..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null){
                attendList.add(Attend(
                        year = row.getCell(1).numericCellValue.toInt(),
                        term = TermType.labelOf(row.getCell(2).toValueString()),
                        course = Course(code = row.getCell(3).toValueString()),
                        section = SectionType.valueOfLabelShort(row.getCell(5).toValueString()),
                        grade = GradeType.labelOf(row.getCell(7).toValueString())
                ).apply { createUserDateAudit(user.id) })
            }
        }

        return attendRepository.mergeAttend(user.id, attendList)
    }

    fun updateAttend(user: UserPrincipal, attendId: Long, request: AttendRequest) =
            attendRepository.updateAttend(user.id, attendId, request.let { Attend(
                    year = it.year,
                    term = it.term,
                    grade = it.grade,
                    section = it.section,
                    course = Course(code = it.courseCode)
            ).apply { updateUserDateAudit(user.id) } })
}