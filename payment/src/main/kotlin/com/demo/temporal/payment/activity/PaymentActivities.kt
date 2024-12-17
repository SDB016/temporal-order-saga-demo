package com.demo.temporal.payment.activity

import com.demo.temporal.payment.common.model.PaymentResult
import io.temporal.activity.ActivityInterface

@ActivityInterface
interface PaymentActivities {
    fun processPayment(orderId: String, amount: Double): PaymentResult
    fun refundPayment(orderId: String): PaymentResult

}