package com.demo.temporal.payment.activity

import com.demo.temporal.payment.exception.PaymentErrorCode
import com.demo.temporal.payment.exception.PaymentException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.failure.ApplicationFailure
import kotlin.random.Random

class PaymentActivitiesImpl : PaymentActivities {
    private val logger = KotlinLogging.logger { }

    override fun processPayment(orderId: String, amount: Double) {
        return try {
            if (amount > 1_000_000) {
                throw PaymentException(PaymentErrorCode.LIMIT_EXCEEDED)
            }
            if (Random.nextInt(100) < 20) {  // 20% 확률로 실패
                throw PaymentException(PaymentErrorCode.SYSTEM_ERROR)
            }

            logger.info { "Order $orderId: 결제 성공" }
        } catch (e: PaymentException) {
            logger.error { "Order $orderId: 결제 실패 - ${e.message}" }
            throw ApplicationFailure.newFailure(
                e.message,
                e.errorCode.name
            )
        }

    }

    override fun refundPayment(orderId: String) {
        return try {
            logger.info { "Order $orderId: 환불 성공" }
        } catch (e: PaymentException) {
            logger.error { "Order $orderId: 환불 실패 - ${e.message}" }
            throw ApplicationFailure.newFailure(
                e.message,
                e.errorCode.code
            )
        }
    }

}