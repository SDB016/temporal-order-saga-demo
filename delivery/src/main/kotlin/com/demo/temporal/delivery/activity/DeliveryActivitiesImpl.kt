package com.demo.temporal.delivery.activity

import com.demo.temporal.delivery.exception.DeliveryErrorCode
import com.demo.temporal.delivery.exception.DeliveryException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.failure.ApplicationFailure


class DeliveryActivitiesImpl : DeliveryActivities {
    private val logger = KotlinLogging.logger {}

    override fun deliverOrder(orderId: String) {
        try {
            // 배송지 유효성 검증
            validateDeliveryAddress(orderId)

            logger.info { "Order $orderId: 배송 출발" }
            Thread.sleep(2000) // 2초 대기

            logger.info { "Order $orderId: 배송중" }
            Thread.sleep(2000) // 2초 대기

            // 배송 완료 전 마지막 1초
            Thread.sleep(1000)
            logger.info { "Order $orderId: 배송 완료" }

        } catch (e: DeliveryException) {
            logger.error { "Order $orderId: 배송 실패 - ${e.message}" }
            throw ApplicationFailure.newFailure(
                e.message,
                e.errorCode.code
            )
        }
    }

    private fun validateDeliveryAddress(orderId: String) {
        // 실제 배송 실패가 발생할 수 있는 시나리오
        when {
            orderId.endsWith("0") -> throw DeliveryException(DeliveryErrorCode.INVALID_ADDRESS)
            orderId.endsWith("9") -> throw DeliveryException(DeliveryErrorCode.AREA_NOT_SUPPORTED)
            orderId.length > 10 -> throw DeliveryException(DeliveryErrorCode.DELIVERY_DISTANCE_EXCEEDED)
        }
    }

    override fun cancelDelivery(orderId: String) {
        try {
            logger.info { "Order $orderId: 배송 취소 요청" }
            // 배송 취소 로직
            logger.info { "Order $orderId: 배송 취소 완료" }
        } catch (e: DeliveryException) {
            logger.error { "Order $orderId: 배송 취소 실패 - ${e.message}" }
            throw ApplicationFailure.newFailure(
                e.message,
                e.errorCode.code
            )
        }
    }
}