package com.demo.temporal.order.model

import com.demo.temporal.order.common.OrderStatus

data class OrderResponse(
    val orderId: String,
    val workflowId: String,
    val status: OrderStatus,
    val message: String,
) {

}
