package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.service.AttendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/attend")
class AttendController @Autowired constructor(
        val attendService: AttendService
){

    @PutMapping("")
    fun updateAttend(attendRequest: AttendRequest) =
            ResponseEntity.ok()
}