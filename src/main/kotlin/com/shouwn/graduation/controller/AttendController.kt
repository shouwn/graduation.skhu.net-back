package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.AttendDataDto
import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.AttendService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/attends")
class AttendController @Autowired constructor(
        val attendService: AttendService,
        val modelMapper: ModelMapper
){

    @PostMapping
    fun addAttendFromFile(@CurrentUser user: UserPrincipal,
                          @RequestParam file: MultipartFile) =
            ResponseEntity.status(HttpStatus.CREATED)
                    .body(attendService.addAttendFromFile(user, file.inputStream))

    @GetMapping("user/{userId}")
    fun findAllUserAttend(@PathVariable userId: Long) =
            ResponseEntity.ok(attendService.attendsByUserId(userId))

    @PutMapping("{attendId}")
    fun modifyAttend(@CurrentUser user: UserPrincipal,
                     @PathVariable("attendId") attendId: Long,
                     @RequestBody attendRequest: AttendRequest) =
            ResponseEntity.ok(attendService.modifyAttend(
                    user.entity,
                    AttendDataDto().apply {
                        modelMapper.map(attendRequest, this)
                    },
                    attendId))

    @PutMapping("{attendId}/replacement/{courseId}")
    fun replaceCourse(@CurrentUser user: UserPrincipal,
                      @PathVariable attendId: Long,
                      @PathVariable courseId: Long) =
            ResponseEntity.ok(attendService.modifyAttend(
                    user.entity,
                    AttendDataDto(courseId = courseId),
                    attendId))
}