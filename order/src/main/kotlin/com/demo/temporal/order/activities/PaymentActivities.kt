package com.demo.temporal.order.activities

interface PaymentActivities {
    fun refundPayment(orderId: String)
    fun processPayment(orderId: String)

}
