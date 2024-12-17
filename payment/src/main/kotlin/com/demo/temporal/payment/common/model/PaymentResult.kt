package com.demo.temporal.payment.common.model

data class PaymentResult(
    val success: Boolean,
    val orderId: String,
    val errorCode: String? = null,
    val errorMessage: String? = null,
)