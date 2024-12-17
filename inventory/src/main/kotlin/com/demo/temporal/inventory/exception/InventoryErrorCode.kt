package com.demo.temporal.inventory.exception

enum class InventoryErrorCode(
    val code: String,
    val message: String
) {
    ITEM_NOT_FOUND("I001", "상품을 찾을 수 없습니다"),
    ITEM_DISCONTINUED("I002", "단종된 상품입니다"),
    OUT_OF_STOCK("I003", "재고가 부족합니다"),
    WAREHOUSE_UNAVAILABLE("I004", "사용 가능한 창고가 없습니다"),
    RESERVATION_FAILED("I005", "재고 할당에 실패했습니다"),
    RESTOCK_FAILED("I006", "재고 원복에 실패했습니다")
}