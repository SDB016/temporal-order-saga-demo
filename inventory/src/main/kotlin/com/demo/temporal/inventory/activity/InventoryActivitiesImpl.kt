package com.demo.temporal.inventory.activity

import io.github.oshai.kotlinlogging.KotlinLogging

class InventoryActivitiesImpl : InventoryActivities {
    private val logger = KotlinLogging.logger { }

    override fun restockInventory(orderId: String) {
        logger.info { "Processing payment for order: $orderId" }

    }

    override fun reserveInventory(orderId: String) {
        logger.info { "Refunding payment for order: $orderId" }

    }

}