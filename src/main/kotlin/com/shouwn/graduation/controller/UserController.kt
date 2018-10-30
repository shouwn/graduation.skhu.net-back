package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.PasswordModifyRequest
import com.shouwn.graduation.model.domain.dto.request.UserDataModifyRequest
import com.shouwn.graduation.model.domain.type.RoleName
import com.shouwn.graduation.model.domain.type.SearchType
import com.shouwn.graduation.repository.UserRepository
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/users")
class UserController @Autowired constructor(
        val userService: UserService
){
    @PutMapping
    fun modifyUserData(@CurrentUser user: UserPrincipal,
                       @RequestBody userDataModifyRequest: UserDataModifyRequest) =
            ResponseEntity.ok(userService.modifyUserData(user, userDataModifyRequest))

    @GetMapping
    fun findUserData(@CurrentUser user: UserPrincipal) =
            ResponseEntity.ok(user.entity)

    @PutMapping("password")
    fun modifyPassword(@CurrentUser user: UserPrincipal,
                       @RequestBody passwordModifyRequest: PasswordModifyRequest) =
            ResponseEntity.ok(userService.modifyPassword(user, passwordModifyRequest).let { "비밀번호 변경 완료" })

    @GetMapping("userData")
    fun findSelfStudentData(@CurrentUser user: UserPrincipal) =
            ResponseEntity.ok(userService.userData(user.entity).let { userService.userResponse(it) })

    @GetMapping("student/{searchTypeValue}/{searchTxt}/{partyId}/{year}/{page}/{size}")
    @Secured("ROLE_ADMIN")
    fun findStudentBySearch(@PathVariable("searchTypeValue") searchTypeValue: Long,
                         @PathVariable("searchTxt") searchTxt: String,
                         @PathVariable("partyId") partyId: Long,
                         @PathVariable("year") year: Int,
                         @PathVariable("page") page: Int,
                         @PathVariable("size") size: Int) =
            ResponseEntity.ok(userService.findUserBySearching(
                    type = SearchType.valueOf(searchTypeValue),
                    searchTxt = searchTxt,
                    role = RoleName.ROLE_STUDENT,
                    partyId = partyId,
                    year = year,
                    page = page,
                    size = size
            ))

    @GetMapping("admin/{searchTypeValue}/{searchTxt}/{partyId}/{year}/{page}/{size}")
    @Secured("ROLE_ADMIN")
    fun findAdminBySearch(@PathVariable("searchTypeValue") searchTypeValue: Long,
                         @PathVariable("searchTxt") searchTxt: String,
                         @PathVariable("partyId") partyId: Long,
                         @PathVariable("page") page: Int,
                         @PathVariable("size") size: Int) =
            ResponseEntity.ok(userService.findUserBySearching(
                    type = SearchType.valueOf(searchTypeValue),
                    searchTxt = searchTxt.trim(),
                    role = RoleName.ROLE_ADMIN,
                    partyId = partyId,
                    year = 0,
                    page = page,
                    size = size
            ))
}