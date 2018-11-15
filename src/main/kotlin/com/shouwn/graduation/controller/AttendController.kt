package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.AttendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/attends")
class AttendController @Autowired constructor(
        val attendService: AttendService
){

    @PostMapping
    fun addAttendFromFile(@CurrentUser user: UserPrincipal,
                          @RequestParam file: MultipartFile) =
            ResponseEntity.status(HttpStatus.CREATED)
                    .body(attendService.addAttendFromFile(user, file.inputStream))

//    @PutMapping("{attendId}")
//    fun updateAttend(@CurrentUser user: UserPrincipal,
//                     @PathVariable("attendId") attendId: Long,
//                     @RequestBody attendRequest: AttendRequest) =
//            ResponseEntity.ok(attendService.updateAttend(user, attendId, attendRequest))
}