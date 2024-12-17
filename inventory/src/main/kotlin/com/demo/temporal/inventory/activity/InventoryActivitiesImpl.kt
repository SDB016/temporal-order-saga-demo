package com.demo.temporal.inventory.activity

import com.demo.temporal.inventory.exception.InventoryErrorCode
import com.demo.temporal.inventory.exception.InventoryException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.failure.ApplicationFailure
import java.util.concurrent.ConcurrentHashMap

data class InventoryItem(
    val stock: Int,
    val warehouses: List<String>
)


class InventoryActivitiesImpl : InventoryActivities {
    private val logger = KotlinLogging.logger {}

    private val inventoryItems = ConcurrentHashMap<String, InventoryItem>().apply {
        put("ITEM_1", InventoryItem(100, listOf("SEOUL", "BUSAN")))
        put("ITEM_2", InventoryItem(0, listOf("BUSAN")))
        put("ITEM_3", InventoryItem(-1, listOf("SEOUL")))
        (4..9).forEach { i ->
            put("ITEM_$i", InventoryItem(50, listOf("SEOUL", "DAEGU")))
        }
    }

    override fun reserveInventory(orderId: String) {
        try {
            logger.info { "Order $orderId: 재고 확인 시작" }

            val itemId = getItemId(orderId)
            val (warehouse, updatedStock) = processInventoryReservation(itemId)

            logger.info { "Order $orderId: $itemId 재고 할당 완료 (남은 수량: $updatedStock, 창고: $warehouse)" }
        } catch (e: Exception) {
            logger.error { "Order $orderId: 재고 할당 실패 - ${e.message}" }
            throw ApplicationFailure.newFailure(
                e.message ?: "Unknown error",
                InventoryErrorCode.RESERVATION_FAILED.code
            )
        }
    }

    override fun restockInventory(orderId: String) {
        try {
            val itemId = getItemId(orderId)
            val item = getInventoryItem(itemId)

            if (item.stock >= 0) {
                inventoryItems[itemId] = item.copy(stock = item.stock + 1)
                logger.info { "Order $orderId: $itemId 재고 원복 완료" }
            }
        } catch (e: Exception) {
            logger.error { "Order $orderId: 재고 원복 실패 - ${e.message}" }
            throw ApplicationFailure.newFailure(
                "재고 원복 실패",
                InventoryErrorCode.RESTOCK_FAILED.code
            )
        }
    }

    private fun getItemId(orderId: String): String =
        "ITEM_${orderId.last()}"

    private fun getInventoryItem(itemId: String): InventoryItem =
        inventoryItems[itemId] ?: throw ApplicationFailure.newFailure(
            "Item not found: $itemId",
            InventoryErrorCode.ITEM_NOT_FOUND.code
        )

    private fun processInventoryReservation(itemId: String): Pair<String, Int> {
        val item = getInventoryItem(itemId)

        when {
            item.stock == -1 -> throw ApplicationFailure.newFailure(
                "Item is discontinued: $itemId",
                InventoryErrorCode.ITEM_DISCONTINUED.code
            )
            item.stock == 0 -> throw ApplicationFailure.newFailure(
                "Out of stock: $itemId",
                InventoryErrorCode.OUT_OF_STOCK.code
            )
            item.warehouses.isEmpty() -> throw ApplicationFailure.newFailure(
                "No warehouse available for item: $itemId",
                InventoryErrorCode.WAREHOUSE_UNAVAILABLE.code
            )
        }

        val warehouse = item.warehouses.first()
        val updatedStock = item.stock - 1
        inventoryItems[itemId] = item.copy(stock = updatedStock)

        return warehouse to updatedStock
    }
}
