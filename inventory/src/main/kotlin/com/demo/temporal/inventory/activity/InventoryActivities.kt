package com.demo.temporal.inventory.activity

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface InventoryActivities {
    fun restockInventory(orderId: String)
    fun reserveInventory(orderId: String)
}