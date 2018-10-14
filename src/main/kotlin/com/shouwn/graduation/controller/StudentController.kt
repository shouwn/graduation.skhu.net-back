package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.AttendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Secured("ROLE_STUDENT")
@RequestMapping("api/student")
class StudentController @Autowired constructor(
        val attendService: AttendService
){
    @PostMapping("attends")
    fun addAttendFromFile(@CurrentUser user: UserPrincipal, file: MultipartFile) =
            ResponseEntity.status(HttpStatus.CREATED).apply { attendService.addAttendFromFile(user, file.inputStream) }
                    .body("수강 과목 업로드 완료")

    @GetMapping("attends")
    fun findAttend(@CurrentUser user: UserPrincipal) =
            ResponseEntity.ok()
}