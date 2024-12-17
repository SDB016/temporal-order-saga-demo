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
    PAYMENT_PROCESSING_ERROR("P002", "결제 처리 중 오류가 발생했습니다.", ServiceType.PAYMENT, HttpStatus.INTERNAL_SERVER_ERROR),

    // Inventory Errors
    ITEM_NOT_FOUND("I001", "상품을 찾을 수 없습니다", ServiceType.INVENTORY, HttpStatus.BAD_REQUEST),
    ITEM_DISCONTINUED("I002", "단종된 상품입니다", ServiceType.INVENTORY, HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK("I003", "재고가 부족합니다", ServiceType.INVENTORY, HttpStatus.BAD_REQUEST),
    WAREHOUSE_UNAVAILABLE("I004", "사용 가능한 창고가 없습니다", ServiceType.INVENTORY, HttpStatus.BAD_REQUEST),
    RESERVATION_FAILED("I005", "재고 할당에 실패했습니다", ServiceType.INVENTORY, HttpStatus.INTERNAL_SERVER_ERROR),
    RESTOCK_FAILED("I006", "재고 원복에 실패했습니다", ServiceType.INVENTORY, HttpStatus.INTERNAL_SERVER_ERROR),

    // Delivery Errors
    INVALID_ADDRESS("D001", "배송지 정보가 올바르지 않습니다", ServiceType.DELIVERY, HttpStatus.BAD_REQUEST),
    AREA_NOT_SUPPORTED("D002", "배송 불가 지역입니다", ServiceType.DELIVERY, HttpStatus.BAD_REQUEST),
    NO_AVAILABLE_COURIER("D003", "현재 배송 가능한 배달원이 없습니다", ServiceType.DELIVERY, HttpStatus.BAD_REQUEST),
    DELIVERY_DISTANCE_EXCEEDED("D004", "배송 가능 거리를 초과했습니다", ServiceType.DELIVERY, HttpStatus.BAD_REQUEST),
    CANCEL_FAILED("D005", "이미 배송이 시작되어 취소할 수 없습니다", ServiceType.DELIVERY, HttpStatus.BAD_REQUEST),

    // Order Errors
    WORKFLOW_START_FAILED("ORD001", "Workflow 시작에 실패했습니다.", ServiceType.ORDER, HttpStatus.INTERNAL_SERVER_ERROR),
    WORKFLOW_STATUS_CHECK_FAILED("ORD002", "Workflow 상태 체크에 실패했습니다.", ServiceType.ORDER, HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    companion object {
        private val errorCodeMap: Map<String, ErrorCode> = values().associateBy { it.code }

        fun fromActivityError(activityFailure: ActivityFailure): ErrorCode {
            val type = (activityFailure.cause as? ApplicationFailure)?.type
            return errorCodeMap[type] ?: throw IllegalArgumentException("Unknown error type: $type")
        }
    }
}