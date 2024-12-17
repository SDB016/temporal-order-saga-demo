package com.demo.temporal.order.model

data class OrderRequest(
    val orderId: String,
    val amount: Double,
)
