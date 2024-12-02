package com.demo.temporal.order.activities

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface InventoryActivities {
    fun restockInventory(orderId: String)
    fun reserveInventory(orderId: String)

}
