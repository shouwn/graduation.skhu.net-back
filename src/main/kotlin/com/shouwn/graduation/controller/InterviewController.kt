package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.InterviewRequest
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.InterviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/interview")
class InterviewController @Autowired constructor(
        val interviewService: InterviewService
){

    @PostMapping("asker/{askerId}")
    fun postInterview(@PathVariable("askerId") askerId: Long,
                      @CurrentUser user: UserPrincipal,
                      @RequestBody @Valid interviewRequest: InterviewRequest): ResponseEntity<*> =
            ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(interviewService.saveInterview(user.entity, askerId, interviewRequest))

    @GetMapping("asker/{askerId}")
    fun findInterviewsByAsker(@PathVariable("askerId") askerId: Long) =
            ResponseEntity.ok(interviewService.findInterviewByAsker(askerId))

    @GetMapping("writer/{writerId}")
    fun findInterviewsByWriter(@PathVariable("writerId") writerId: Long) =
            ResponseEntity.ok(interviewService.findInterviewByWriter(writerId))

    @PutMapping("update/{interviewId}")
    fun updateInterview(@PathVariable interviewId: Long,
                        @Valid @RequestBody interviewRequest: InterviewRequest,
                        @CurrentUser user: UserPrincipal) =
            ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(interviewService.updateInterview(interviewId, interviewRequest, user))
}