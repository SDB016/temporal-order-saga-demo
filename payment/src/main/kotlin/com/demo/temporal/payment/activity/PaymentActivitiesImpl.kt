package com.demo.temporal.payment.activity

import com.demo.temporal.payment.common.model.PaymentResult
import com.demo.temporal.payment.exception.ErrorCode
import com.demo.temporal.payment.exception.PaymentException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.failure.ApplicationFailure
import kotlin.random.Random

class PaymentActivitiesImpl : PaymentActivities {
    private val logger = KotlinLogging.logger { }

    override fun processPayment(orderId: String, amount: Double): PaymentResult {
        return try {
            if (amount > 1_000_000) {
                throw PaymentException(ErrorCode.LIMIT_EXCEEDED)
            }
            if (Random.nextInt(100) < 20) {  // 20% 확률로 실패
                throw PaymentException(ErrorCode.SYSTEM_ERROR)
            }

            logger.info { "Payment processed successfully for order $orderId" }
            PaymentResult(true, orderId)
        } catch (e: PaymentException) {
            logger.error { "Payment failed for order $orderId: ${e.message}" }
            throw ApplicationFailure.newFailure(
                e.message ?: "Unknown error",
                e.errorCode.name
            )
        }

    }

    override fun refundPayment(orderId: String): PaymentResult {
        return try {
            logger.info { "Refund processed successfully for order $orderId" }
            PaymentResult(true, orderId)
        } catch (e: Exception) {
            logger.error { "Refund failed for order $orderId: ${e.message}" }
            PaymentResult(false, orderId, "REFUND_FAILED", e.message)
        }
    }

}