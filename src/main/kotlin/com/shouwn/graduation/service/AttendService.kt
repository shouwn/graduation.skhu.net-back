package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.db.AttendDto
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.type.GradeType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType
import com.shouwn.graduation.repository.AttendRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.value
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDateTime

@Service
class AttendService @Autowired constructor(
        val attendRepository: AttendRepository
){
    fun addAttendFromFile(user: UserPrincipal, file: InputStream){

        val attendList = arrayListOf<AttendDto>()

        val sheet = WorkbookFactory.create(file).getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        // TODO(엑셀 서식 체크)

        // TODO(해당 과목이 없을 경우 체크)

        for(rowIndex in 2..rows){
            val row = sheet.getRow(rowIndex)

            if(row != null){
                attendList.add(AttendDto(
                        year = row.getCell(1).numericCellValue.toInt(),
                        term = TermType.labelOf(row.getCell(2).value()).value,
                        courseCode =  row.getCell(3).value(),
                        section = SectionType.valueOfLabelShort(row.getCell(5).value()).value,
                        grade = GradeType.labelOf(row.getCell(7).value()).value,
                        userNumber = user.username,
                        createdBy = user.id,
                        updatedBy = user.id,
                        updatedAt = LocalDateTime.now().toString()
                ))
            }
        }

        attendRepository.mergeAttend(attendList)
    }
}