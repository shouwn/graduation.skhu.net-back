package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.dto.request.InterviewRequest
import com.shouwn.graduation.model.domain.entity.Interview
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.repository.InterviewRepository
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class InterviewService @Autowired constructor(
        val interviewRepository: InterviewRepository,
        val userRepository: UserRepository
){

    fun saveInterview(writer: User, askerId: Long, interviewRequest: InterviewRequest): Interview =
            Interview(
                    title = interviewRequest.title,
                    content = interviewRequest.content,
                    writer = writer,
                    asker = userRepository.findById(askerId).get()
            ).let { interviewRepository.save(it) }

    fun findInterviewByAsker(askerId: Long) =
            interviewRepository.findAllByAskerId(askerId)

    fun findInterviewByWriter(writerId: Long) =
            interviewRepository.findAllByWriterId(writerId)

    fun updateInterview(interviewId: Long, interviewRequest: InterviewRequest, user: UserPrincipal) =
            interviewRepository.findById(interviewId).get().takeIf { it.writer?.id == user.id }
                    ?.let { interviewRepository.save(it.copy(
                            title = interviewRequest.title,
                            content = interviewRequest.content
                    )) }
                    ?: throw ApiException(HttpStatus.BAD_REQUEST, ApiResponse(false, "작성자가 다릅니다."))
}