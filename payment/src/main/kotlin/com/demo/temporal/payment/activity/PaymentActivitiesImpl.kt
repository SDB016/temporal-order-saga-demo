package com.demo.temporal.payment.activity

import io.github.oshai.kotlinlogging.KotlinLogging

class PaymentActivitiesImpl : PaymentActivities {
    private val logger = KotlinLogging.logger { }

    override fun processPayment(orderId: String) {
        logger.info { "Processing payment for order: $orderId" }

    }

    override fun refundPayment(orderId: String) {
        logger.info { "Refunding payment for order: $orderId" }

    }

}