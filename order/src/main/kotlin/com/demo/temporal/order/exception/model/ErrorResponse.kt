package com.demo.temporal.order.exception.model

data class ErrorResponse(
    val code: String,
    val message: String,
    val service: String,
    val details: Map<String, Any>? = null
) {
}