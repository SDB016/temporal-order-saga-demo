package com.demo.temporal.delivery.exception

enum class DeliveryErrorCode(
    val code: String,
    val message: String
) {
    INVALID_ADDRESS("D001", "배송지 정보가 올바르지 않습니다"),
    AREA_NOT_SUPPORTED("D002", "배송 불가 지역입니다"),
    NO_AVAILABLE_COURIER("D003", "현재 배송 가능한 배달원이 없습니다"),
    DELIVERY_DISTANCE_EXCEEDED("D004", "배송 가능 거리를 초과했습니다"),
    CANCEL_FAILED("D005", "이미 배송이 시작되어 취소할 수 없습니다")
}
