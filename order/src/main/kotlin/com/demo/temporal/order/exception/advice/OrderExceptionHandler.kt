package com.demo.temporal.order.exception.advice

import com.demo.temporal.order.exception.ErrorCode
import com.demo.temporal.order.exception.OrderException
import com.demo.temporal.order.exception.model.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.failure.ActivityFailure
import io.temporal.failure.ApplicationFailure
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class OrderExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ActivityFailure::class)
    fun handleActivityFailure(e: ActivityFailure): ResponseEntity<ErrorResponse> {
        logger.error { "Activity failure: ${e.message}" }
        return buildErrorResponse(ErrorCode.fromActivityError(e), e.message)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.error(e) { "IllegalArgumentException: ${e.message}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(
                code = "01",
                message = "IllegalArgumentException",
                service = "ORDER"
            ))
    }

    @ExceptionHandler(OrderException::class)
    fun handleOrderException(e: OrderException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        return buildErrorResponse(errorCode, e.message ?: errorCode.message)
    }

    private fun buildErrorResponse(errorCode: ErrorCode, message: String?): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ErrorResponse(
                code = errorCode.code,
                message = message ?: errorCode.message,
                service = errorCode.serviceType.name
            ))
    }
}