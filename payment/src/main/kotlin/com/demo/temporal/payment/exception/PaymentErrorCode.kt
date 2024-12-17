package com.demo.temporal.payment.exception


enum class PaymentErrorCode(val code: String, val message: String) {
    LIMIT_EXCEEDED("P001", "결제 한도를 초과했습니다."),
    SYSTEM_ERROR("P002", "결제 시스템 일시적 오류"),

}

