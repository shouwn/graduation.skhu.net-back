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
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.UnsupportedOperationException

@Service
class RequirementService @Autowired constructor(
        private val requirementRepository: RequirementRepository,
        private val partyService: PartyService,
        private val courseService: CourseService,
        private val userRepository: UserRepository
){
    private val logger = logger()

    @Transactional
    fun addRequirement(userId: Long, request: RequirementRequest){
        val requirement = Requirement(
                name = request.name,
                satisfying = request.satisfyingType,
                need = request.need,
                clazzMin = request.clazzMin ?: 0,
                clazzMax = request.clazzMax ?: 9999
        ).apply {
            createUserDateAudit(userId)
            party = request.party?.let { partyService.findPartiesByIds(listOf(it)).first() }
        }

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
    fun modifyRequirement(userId: Long, requirementId: Long, request: RequirementRequest) =
            requirementRepository.findById(requirementId)
                    .orElseThrow {
                        ApiException(
                                status = HttpStatus.NOT_FOUND,
                                apiResponse = ApiResponse(
                                        success = false,
                                        message = "${requirementId}에 해당하는 요건이 없습니다."
                                )
                        )
                    }.let {
                        it.copy(
                                name = request.name,
                                satisfying = request.satisfyingType,
                                need = request.need,
                                clazzMin = request.clazzMin ?: 0,
                                clazzMax = request.clazzMax ?: 9999
                        ).apply {
                            this.party = request.party?.let { partyService.findPartiesByIds(listOf(it)).first() }
                            when(this.satisfying){
                                in SatisfyingType.COURSE_SET ->
                                    this.courses = courseService.findCoursesByIds(request.target)
                                SatisfyingType.CHILDREN ->
                                    this.subs = this@RequirementService.findRequirementByIds(request.target)
                                else -> { }
                            }
                            updateUserDateAudit(userId, it)
                            this@RequirementService.requirementRepository.save(this, 1)
                        }
                    }

    fun findAllByName(name: String) =
            requirementRepository.findAllLikeName(".*$name.*")

    fun findGeneralRequirements() =
            requirementRepository.findAllSubs()
                    .asSequence()
                    .find { it.name == "졸업" }
                    ?: throw ApiException(
                            status = HttpStatus.NOT_FOUND,
                            apiResponse = ApiResponse(
                                    success = false,
                                    message = "졸업 요건이 없습니다. 관리자에게 문의하세요."
                            )
                    )

    fun findMajorRequirements(partyId: Long) =
            requirementRepository.findAllSubs()
                    .asSequence()
                    .find { it.party?.id == partyId }
                    ?: throw ApiException(
                            status = HttpStatus.NOT_FOUND,
                            apiResponse = ApiResponse(
                                    success = false,
                                    message = "해당 학과에 졸업학과가 없습니다."
                            )
                    )

    @Transactional
    fun checkGraduation(userId: Long): Int{
        val user = findAllById(userRepository, setOf(userId)).first()

        if(user.requirement == null)
            throw ApiException(
                    status = HttpStatus.PRECONDITION_FAILED,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "졸업 요건이 선택되지 않았습니다,"
                    )
            )

        val requirements = requirementRepository.findAllSubs()

        val generalRequirement = requirements.asSequence()
                .filter { it.name == "졸업" }
                .first()

        val majorRequirement = requirements
                .asSequence()
                .filter { it == user.requirement!! }
                .first()

        return this.isMeet(generalRequirement, user.attends!!.toSet(), user)
    }

    private fun isMeet(requirement: Requirement, attends: Set<Attend>, user: User): Int {

        if(user.userNumber.substring(0, 4).toLong().let { it > requirement.clazzMax
                        || it < requirement.clazzMin
                }) { // 학번 확인
            return 0
        }
        if(requirement.party?.let { user.parties?.contains(it) } == true)
            return 0

        logger.info("${requirement.name} 체크 중")

        return try { when(requirement.satisfying){
            SatisfyingType.COURSE_CREDIT -> {
                check(attends.filterCredit { it.course in requirement.courses!! }
                        .reduce { acc, d -> acc + d } >= requirement.need, requirement.name)
            }
            SatisfyingType.COURSE_COUNT -> {
                check(attends.asSequence().filter { it.course in requirement.courses!! }
                        .count() >= requirement.need, requirement.name)
            }
            SatisfyingType.CHILDREN -> {
                check(requirement.subs!!.asSequence().map { this.isMeet(it, attends, user) }
                        .reduce { acc, i -> acc + i } >= requirement.need, requirement.name)
            }
            SatisfyingType.GENERAL -> {
                check(attends.filterCredit { it.section in SectionType.GENERAL_SET }
                        .reduce { acc, d -> acc + d } >= requirement.need, requirement.name)
            }
            SatisfyingType.MAJOR -> {
                check(attends.filterCredit { it.section in SectionType.MAJOR_SET }
                        .reduce { acc, d -> acc + d } >= requirement.need, requirement.name)
            }
            SatisfyingType.MINOR -> {
                check(attends.filterCredit { it.section in SectionType.MINOR_SET }
                        .reduce { acc, d -> acc + d } >= requirement.need, requirement.name)
            }
            SatisfyingType.ALL -> {
                check(attends.filterCredit { true }
                        .reduce { acc, d -> acc + d } >= requirement.need, requirement.name)
            }
        }}
        catch (e: UnsupportedOperationException) {
            0
        }
    }

    fun findRequirementByIds(ids: Iterable<Long>) =
            findAllById(requirementRepository, ids).toSet()

    private fun Set<Attend>.filterCredit(predicate: (Attend) -> Boolean) =
            this.asSequence().filter(predicate)
                    .map { it.credit }

    private fun check(bool: Boolean, requirementName: String) =
            if(bool)
                logger.info("$requirementName 합격").let { 1 }
            else
                logger.info("$requirementName 불합격").let { 0 }

}