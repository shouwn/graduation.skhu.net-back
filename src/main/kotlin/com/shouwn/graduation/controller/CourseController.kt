package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.CourseRequest
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

    @PutMapping("{courseId}")
    @Secured("ROLE_ADMIN")
    fun updateCourse(@CurrentUser user: UserPrincipal,
                     @PathVariable("courseId") courseId: Long,
                     @RequestBody courseRequest: CourseRequest) =
            ResponseEntity.ok(courseService.updateCourse(user, courseId, courseRequest))

    @GetMapping("{code}/{name}/{partyName}")
    fun findCoursesByCodeOrName(@PathVariable("code") code: String,
                                @PathVariable("name") name: String,
                                @PathVariable("partyName") partyName: String) =
            ResponseEntity.ok(courseService.findCoursesLikeCodeAndName(code.trim(), name.trim(), partyName.trim()))

    @PutMapping("disable/{courseId}")
    @Secured("ROLE_ADMIN")
    fun disableCourse(@CurrentUser user: UserPrincipal,
                      @PathVariable courseId: Long) =
            ResponseEntity.ok(courseService.disableCourse(user.entity, courseId))

    @PostMapping("replace")
    @Secured("ROLE_ADMIN")
    fun replaceByFile(@CurrentUser user: UserPrincipal,
                      @RequestParam file: MultipartFile) =
            ResponseEntity.noContent()
                    .apply { courseService.setReplaceByFile(user.entity, file.inputStream) }
                    .build<String>()

    @GetMapping("{courseId}/replace")
    fun findCanReplacedCourse(@PathVariable courseId: Long) =
            ResponseEntity.ok(courseService.findCourseCanReplaced(courseId))
}