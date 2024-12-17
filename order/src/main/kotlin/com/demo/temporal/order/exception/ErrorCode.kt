package com.demo.temporal.order.exception

import com.demo.temporal.order.common.ServiceType
import io.temporal.failure.ActivityFailure
import io.temporal.failure.ApplicationFailure
import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val serviceType: ServiceType,
    val httpStatus: HttpStatus,
) {
    // Payment Errors
    PAYMENT_INVALID_AMOUNT("P001", "결제 금액이 유효하지 않습니다.", ServiceType.PAYMENT, HttpStatus.BAD_REQUEST),
    PAYMENT_PROCESSING_ERROR("P003", "결제 처리 중 오류가 발생했습니다.", ServiceType.PAYMENT, HttpStatus.INTERNAL_SERVER_ERROR),

    // Inventory Errors

    // Delivery Errors

    // Order Errors
    WORKFLOW_START_FAILED("ORD001", "Workflow 시작에 실패했습니다.", ServiceType.ORDER, HttpStatus.INTERNAL_SERVER_ERROR),
    WORKFLOW_STATUS_CHECK_FAILED("ORD002", "Workflow 상태 체크에 실패했습니다.", ServiceType.ORDER, HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    companion object {

        fun fromActivityError(activityFailure: ActivityFailure): ErrorCode {
            val type = (activityFailure.cause as? ApplicationFailure)?.type
            return when (type) {
                "11" -> PAYMENT_INVALID_AMOUNT
                "85" -> PAYMENT_PROCESSING_ERROR
                else -> throw IllegalArgumentException("Unknown error type: $type")
            }
        }
    }
}