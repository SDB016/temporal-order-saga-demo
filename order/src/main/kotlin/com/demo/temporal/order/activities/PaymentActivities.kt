package com.demo.temporal.order.activities

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface PaymentActivities {
    fun refundPayment(orderId: String)
    fun processPayment(orderId: String)

}
