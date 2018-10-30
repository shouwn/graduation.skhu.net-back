package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.RequirementRequest
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.RequirementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/requirements")
class RequirementController @Autowired constructor(
        val requirementService: RequirementService
){

    @PostMapping()
    @Secured("ROLE_ADMIN")
    fun addRequirement(@CurrentUser user: UserPrincipal,
                       @RequestBody requirementRequest: RequirementRequest) =
            ResponseEntity.status(HttpStatus.CREATED)
                    .body(requirementService.addRequirement(user.entity.id!!, requirementRequest).let { "요건 생성 완료" })

    @GetMapping("users/{userId}")
    fun checkGraduate(@PathVariable("userId") userId: Long) =
            ResponseEntity.ok(requirementService.checkGraduation(userId))

    @GetMapping("general")
    fun generalRequirements() =
            ResponseEntity.ok(requirementService.findGeneralRequirements())

    @GetMapping("major/{partyId}")
    fun majorRequirements(@PathVariable("partyId") partyId: Long) =
            ResponseEntity.ok(requirementService.findMajorRequirements(partyId))

    @PutMapping("{requirementId}")
    @Secured("ROLE_ADMIN")
    fun modifyRequirement(@CurrentUser user: UserPrincipal,
                          @PathVariable("requirementId") requirementId: Long,
                          @RequestBody request: RequirementRequest) =
            ResponseEntity.ok(requirementService.modifyRequirement(user.id, requirementId, request))
}