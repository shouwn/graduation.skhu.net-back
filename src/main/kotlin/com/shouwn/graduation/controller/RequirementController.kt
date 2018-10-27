package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.RequirementRequest
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.RequirementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/requirement")
class RequirementController @Autowired constructor(
        val requirementService: RequirementService
){

    @PostMapping()
    @Secured("ROLE_ADMIN")
    fun addRequirement(@CurrentUser user: UserPrincipal,
                       @RequestBody requirementRequest: RequirementRequest) =
            ResponseEntity.status(HttpStatus.CREATED)
                    .body(requirementService.addRequirement(user.entity.id!!, requirementRequest).let { "요건 생성 완료" })
}