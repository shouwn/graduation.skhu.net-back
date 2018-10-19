package com.shouwn.graduation.controller

import com.shouwn.graduation.model.domain.dto.request.PartyRequest
import com.shouwn.graduation.security.CurrentUser
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.service.PartyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/parties")
class PartyController @Autowired constructor(
        val partyService: PartyService
){
    @GetMapping
    fun partyList(@CurrentUser user: UserPrincipal) =
            ResponseEntity.ok(partyService.findParties(user))

    @PostMapping
    @Secured("ROLE_ADMIN")
    fun addParty(@CurrentUser user: UserPrincipal,
                 @RequestBody partyRequest: PartyRequest) =
            ResponseEntity.ok(partyService.saveParty(user, partyRequest))

    @PutMapping("{partyId}")
    @Secured("ROLE_ADMIN")
    fun updateParty(@CurrentUser user: UserPrincipal,
                    @PathVariable("partyId") partyId: Long,
                    @RequestBody partyRequest: PartyRequest) =
            ResponseEntity.ok(partyService.updateParty(user, partyId, partyRequest))
}