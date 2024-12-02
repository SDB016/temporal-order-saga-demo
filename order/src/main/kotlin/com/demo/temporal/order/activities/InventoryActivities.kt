package com.demo.temporal.order.activities

interface InventoryActivities {
    fun restockInventory(orderId: String)
    fun reserveInventory(orderId: String)

}
