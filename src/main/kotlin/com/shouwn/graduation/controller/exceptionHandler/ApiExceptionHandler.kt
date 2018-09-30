package com.shouwn.graduation.controller.exceptionHandler

import com.shouwn.graduation.model.domain.exception.ApiException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestController
class ApiExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(request: HttpServletRequest, ex: ApiException): ResponseEntity<*> =
            ResponseEntity.status(ex.status).body(ex.apiResponse)
}