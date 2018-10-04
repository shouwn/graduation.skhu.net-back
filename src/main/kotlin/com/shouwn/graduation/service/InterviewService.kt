package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.entity.Interview
import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.repository.InterviewRepository
import com.shouwn.graduation.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class InterviewService @Autowired constructor(
        val interviewRepository: InterviewRepository,
        val userRepository: UserRepository
){

    fun saveInterview(writer: User, askerId: Long, interview: Interview): Interview =
            interview.copy(
                    writer = writer,
                    asker = userRepository.findById(askerId).get()
            ).let { interviewRepository.save(it) }
}