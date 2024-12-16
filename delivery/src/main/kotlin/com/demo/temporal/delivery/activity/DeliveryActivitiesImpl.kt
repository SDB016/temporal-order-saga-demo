package com.demo.temporal.delivery.activity

import io.github.oshai.kotlinlogging.KotlinLogging

class DeliveryActivitiesImpl : DeliveryActivities {
    private val logger = KotlinLogging.logger { }

    override fun deliverOrder(orderId: String) {
        logger.info{ "Delivering order for $orderId" }
    }

    override fun cancelDelivery(orderId: String) {
        logger.info { "Cancelling order for $orderId" }
    }
}