package com.shouwn.graduation.model.domain.exception

import com.shouwn.graduation.model.domain.dto.ApiResponse
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class ApiException constructor(
        val status: HttpStatus,
        val apiResponse: ApiResponse
) : RuntimeException()