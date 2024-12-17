package com.demo.temporal.delivery.exception

class DeliveryException(
    val errorCode: DeliveryErrorCode,
    override val message: String = errorCode.message,
) : RuntimeException(message) {
}