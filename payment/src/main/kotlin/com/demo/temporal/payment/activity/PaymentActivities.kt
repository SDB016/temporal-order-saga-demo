package com.demo.temporal.payment.activity

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface PaymentActivities {
    fun processPayment(orderId: String, amount: Double)
    fun refundPayment(orderId: String)

}