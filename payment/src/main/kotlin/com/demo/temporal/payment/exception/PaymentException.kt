package com.demo.temporal.payment.exception

class PaymentException(
    val errorCode: PaymentErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message) {

}
