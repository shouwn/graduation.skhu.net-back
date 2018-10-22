package com.shouwn.graduation.service

import com.shouwn.graduation.model.domain.dto.request.PartyRequest
import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.exception.ApiException
import com.shouwn.graduation.model.domain.type.BelongType
import com.shouwn.graduation.repository.PartyRepository
import com.shouwn.graduation.security.UserPrincipal
import com.shouwn.graduation.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PartyService @Autowired constructor(
        val partyRepository: PartyRepository
){
    private val logger = logger()

    @Transactional
    fun saveParty(user: UserPrincipal, request: PartyRequest) =
            if(partyRepository.existByName(request.name))
                throw ApiException(
                        status = HttpStatus.CONFLICT,
                        apiResponse = ApiResponse(
                                success = false,
                                message = "${request.name}라는 이름이 이미 존재합니다."
                        )
                ).apply { logger.error("${user.id}가 이미 있는 ${request.name} 소속을 만들려고 시도함") }
            else
                partyRepository.save(Party(
                        name = request.name,
                        belong = request.belong
                ).apply { createUserDateAudit(user.id) })
                        .also { logger.info("${user.id}가 ${request.name} 소속을 만듬") }

    fun findParties(user: UserPrincipal): MutableIterable<Party> =
            partyRepository.findAll()
                    .apply { logger.info("${user.id}가 소속 목록을 조회") }

    @Transactional
    fun updateParty(user: UserPrincipal, partyId: Long, request: PartyRequest): Party {
        val old = partyRepository.findById(partyId)

        if(!old.isPresent)
            throw ApiException(
                    status = HttpStatus.NOT_FOUND,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "${partyId}에 해당하는 소속이 없습니다."
                    )
            ).apply { logger.error("${user.id}가 없는 소속을 변경하려고 시도함") }

        val oldName = old.get().name

        if(partyRepository.existByName(request.name))
            throw ApiException(
                    status = HttpStatus.CONFLICT,
                    apiResponse = ApiResponse(
                            success = false,
                            message = "${request.name}라는 이름이 이미 존재합니다."
                    )
            ).apply { logger.error("${user.id}가 ${oldName}를 이미 있는 ${request.name} 소속으로 이름을 변경하려고 시도") }

        return partyRepository.updateById(partyId, Party(
                name = request.name,
                belong = request.belong
        ).apply { updateUserDateAudit(user.id) })
                .apply { logger.info("${user.id}가 ${oldName}을 ${this.name}으로 이름 변경") }
    }

    fun findPartiesByBelongType(belong: BelongType) =
            partyRepository.findAllByBelong(belong)

}