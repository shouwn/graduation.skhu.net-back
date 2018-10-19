package com.shouwn.graduation.controller

import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/courses")
class CourseController @Autowired constructor(
        val courseService: CourseService
){
    @PostMapping
    @Secured("ROLE_ADMIN")
    fun addCoursesFromFile(@CurrentUser user: UserPrincipal,
                           @RequestBody file: MultipartFile) =
            ResponseEntity.status(HttpStatus.CREATED)
                    .body(courseService.addCourseFromFile(user, file.inputStream))
}