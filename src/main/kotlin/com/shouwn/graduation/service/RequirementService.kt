package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.CoursePrincipal
import com.shouwn.graduation.model.domain.dto.RequirementPrincipal
import com.shouwn.graduation.model.domain.dto.request.RequirementRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.entity.Requirement
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.SatisfyingType
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.repository.RequirementRepository
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.utils.findAllById
import com.shouwn.graduation.utils.logger
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import java.util.*

@Service
class RequirementService @Autowired constructor(
        private val requirementRepository: RequirementRepository,
        private val partyService: PartyService,
        private val courseService: CourseService,
        private val userRepository: UserRepository,
        private val modelMapper: ModelMapper
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
                    .filter { it.party?.id == partyId }
                    .toList()

    @Transactional
    fun checkGraduation(userId: Long): List<RequirementPrincipal>{
        val user = findAllById(userRepository, setOf(userId)).first()

//        if(user.requirements.isNullOrEmpty())
//            throw ApiException(
//                    status = HttpStatus.PRECONDITION_FAILED,
//                    apiResponse = ApiResponse(
//                            success = false,
//                            message = "졸업 요건이 선택되지 않았습니다,"
//                    )
//            )

        val requirements = requirementRepository.findAllSubs()
                .asSequence()
                .map {
                    RequirementPrincipal().apply {
                        modelMapper.map(it, this)
                    }
                }.toList()

        return requirements.asSequence()
                .filter { it.name == "졸업" || user.requirements?.map { r -> r.id }?.contains(it.id) ?: false }
                .onEach { requirement ->
                    user.attends?.let { isMeet(requirement, it.toSet(), user) }
                }.toList()
                .let { sortRequirements(it) }
    }

    private fun sortRequirements(requirements: Iterable<RequirementPrincipal>): List<RequirementPrincipal> {
        requirements.asSequence()
                .filter { it.subs?.isNotEmpty() ?: false }
                .forEach { it.subs = sortRequirements(it.subs!!).toMutableSet() }

        return requirements.sortedWith(compareBy(RequirementPrincipal::id))
    }

    private fun isMeet(requirement: RequirementPrincipal, attends: Set<Attend>, user: User): Int {

        if(user.userNumber.substring(0, 4).toLong().let { it > requirement.clazzMax!!
                        || it < requirement.clazzMin!!
                }) { // 학번 확인
            throw IllegalStateException()
        }
        if(requirement.party?.let { user.parties?.contains(it) } == false)
            throw IllegalStateException()

        val requirementCourseIds =
                requirement.courses?.map { it.id }

        val courseMap = requirement.courses?.associate { it.id!! to it }

        requirement.isMeet = try { when(requirement.satisfying!!){
            SatisfyingType.COURSE_CREDIT -> {
                attends.asSequence().filter { it.course?.id in requirementCourseIds!! }
                        .onEach { courseMap?.get(it.course?.id)?.isMeet = true }
                        .map { it.credit }
                        .reduce { acc, d -> acc + d } >= requirement.need!!
            }
            SatisfyingType.COURSE_COUNT -> {
                attends.asSequence().filter { it.course?.id in requirementCourseIds!! }
                        .onEach { courseMap?.get(it.course?.id)?.isMeet = true }
                        .map { it.credit }
                        .count() >= requirement.need!!
            }
            SatisfyingType.CHILDREN -> {
                val removedRequirements = mutableSetOf<RequirementPrincipal>()

                requirement.subs!!.asSequence().map {
                    try {
                        this.isMeet(it, attends, user)
                    } catch (e: IllegalStateException){
                        removedRequirements.add(it)
                        0
                    }
                }.reduce { acc, i -> acc + i } >= requirement.need!!
                        .apply {
                            requirement.subs!!.removeAll(removedRequirements)
                        }
            }
            SatisfyingType.GENERAL -> {
                attends.filterCredit { it.section in SectionType.GENERAL_SET }
                        .reduce { acc, d -> acc + d } >= requirement.need!!
            }
            SatisfyingType.MAJOR -> {
                attends.filterCredit { it.section in SectionType.MAJOR_SET }
                        .reduce { acc, d -> acc + d } >= requirement.need!!
            }
            SatisfyingType.MINOR -> {
                attends.filterCredit { it.section in SectionType.MINOR_SET }
                        .reduce { acc, d -> acc + d } >= requirement.need!!
            }
            SatisfyingType.ALL -> {
                attends.filterCredit { true }
                        .reduce { acc, d -> acc + d } >= requirement.need!!
            }
        }} catch (e: UnsupportedOperationException) {
            false
        }

        return requirement.isMeet.let { if(it) 1 else 0 }
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