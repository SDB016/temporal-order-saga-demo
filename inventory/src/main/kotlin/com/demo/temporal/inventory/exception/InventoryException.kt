package com.demo.temporal.inventory.exception

class InventoryException(
    val errorCode: InventoryErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message) {

}
