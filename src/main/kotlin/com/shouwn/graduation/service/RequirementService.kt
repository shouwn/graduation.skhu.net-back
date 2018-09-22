package com.shouwn.graduation.service

import com.shouwn.graduation.repository.RequirementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RequirementService @Autowired constructor(
        val requirementRepository: RequirementRepository
){
}