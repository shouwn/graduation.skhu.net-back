package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.RequirementRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Requirement
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.SatisfyingType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.repository.RequirementRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RequirementService @Autowired constructor(
        private val requirementRepository: RequirementRepository,
        private val partyService: PartyService,
        private val courseService: CourseService
){
    @Transactional
    fun addRequirement(userId: Long, request: RequirementRequest){
        val requirement = Requirement(
                name = request.name,
                satisfying = request.satisfyingType,
                need = request.need,
                clazz = request.clazz,
                party = partyService.findPartiesByPartyIds(listOf(request.party)).first()
        ).apply { createUserDateAudit(userId) }

        when(requirement.satisfying){
            in SatisfyingType.COURSE_SET ->
                requirement.courses = courseService.findCoursesByIds(request.target)
            SatisfyingType.CHILDREN ->
                requirement.subs = this.findRequirementByIds(request.target)
            else -> { }
        }

        requirementRepository.save(requirement)
    }

    @Transactional
    fun checkGraduation(user: User){
        if(user.requirement == null)
            throw ApiException(
                    status = HttpStatus.PRECONDITION_FAILED,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "졸업 요건이 선택되지 않았습니다,"
                    )
            )

        val requirement = requirementRepository.findByName("졸업")

    }

    fun isMeet(requirement: Requirement, attends: List<Attend>, user: User): Int {

        if(user.userNumber.substring(2, 4).toLong() > requirement.clazz) // 학번 확인
            return 0

        if(user.parties?.contains(requirement.party) != true)
            return 0

        val courses = attends.asSequence().map { it.course }.toList()

        return when(requirement.satisfying){
            SatisfyingType.COURSE_CREDIT -> {
                if(requirement.courses!!.asSequence().filter { it in courses}
                                .map { it.credit }
                                .reduce { acc, d -> d?.let { acc?.plus(it) } }!! >= requirement.need)
                    1
                else 0
            }
            SatisfyingType.COURSE_COUNT -> {
                if(requirement.courses!!.asSequence().filter { it in courses }
                        .count() >= requirement.need)
                    1
                else 0
            }
            SatisfyingType.CHILDREN -> {
                if(requirement.subs!!.asSequence().map { this.isMeet(it, attends, user) }
                                .reduce { acc, i -> acc + i } >= requirement.need)
                    1
                else 0
            }
            SatisfyingType.GENERAL -> {
                if(attends.asSequence().filter { it.section in SectionType.GENERAL_SET }
                                .map { it.course!!.credit }
                                .reduce { acc, d -> d?.let { acc?.plus(it) } }!! >= requirement.need)
                    1
                else 0
            }
            SatisfyingType.MAJOR -> {
                if(attends.asSequence().filter { it.section in SectionType.MAJOR_SET }
                                .map { it.course!!.credit }
                                .reduce { acc, d -> d?.let { acc?.plus(it) } }!! >= requirement.need)
                    1
                else 0
            }
            SatisfyingType.MINOR -> {
                if(attends.asSequence().filter { it.section in SectionType.MINOR_SET }
                                .map { it.course!!.credit }
                                .reduce { acc, d -> d?.let { acc?.plus(it) } }!! >= requirement.need)
                    1
                else 0
            }
        }
    }

    fun findRequirementByIds(ids: Iterable<Long>) =
            findAllById(requirementRepository, ids).toSet()

}