package com.shouwn.graduation.utils

import com.shouwn.graduation.model.domain.dto.response.ApiResponse
import com.shouwn.graduation.model.domain.exception.ApiException
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.http.HttpStatus

fun <T>findAllById(neo4jRepository: Neo4jRepository<out T, Long>,
                ids: Iterable<Long>): MutableIterable<T> =
        neo4jRepository.findAllById(ids).apply {
            if(this.count() != ids.count())
                throw ApiException(
                        status = HttpStatus.PRECONDITION_FAILED,
                        apiResponse = ApiResponse(
                                success = false,
                                message = "$ids 중 해당하지 않는 소속이 있습니다."
                        )
                )
        }