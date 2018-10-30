package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.Hint
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.model.domain.type.SearchType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/list")
class ListController {

    @GetMapping("searchTypes")
    fun searchTypes() =
            ResponseEntity.ok(SearchType.values())

    @GetMapping("hints")
    fun hintList() =
            ResponseEntity.ok(Hint.values())

    @GetMapping("belongs")
    fun belongType() =
            ResponseEntity.ok(BelongType.values())
}