package com.demo.temporal.payment.exception


enum class ErrorCode(val code: String, val message: String) {
    LIMIT_EXCEEDED("11", "결제 한도를 초과했습니다."),
    SYSTEM_ERROR("85", "결제 시스템 일시적 오류"),

}

