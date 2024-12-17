package com.demo.temporal.order.workflow

import com.demo.temporal.delivery.activity.DeliveryActivities
import com.demo.temporal.inventory.activity.InventoryActivities
import com.demo.temporal.order.common.model.OrderResult
import com.demo.temporal.payment.activity.PaymentActivities
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Saga
import io.temporal.workflow.Workflow
import java.time.Duration
import org.springframework.stereotype.Component

@Component
class OrderWorkflowImpl : OrderWorkflow {

    private val paymentActivities: PaymentActivities by lazy {
        createActivityStub(PaymentActivities::class.java, "PaymentTaskQueue")
    }

    private val inventoryActivities: InventoryActivities by lazy {
        createActivityStub(InventoryActivities::class.java, "InventoryTaskQueue")
    }

    private val deliveryActivities: DeliveryActivities by lazy {
        createActivityStub(DeliveryActivities::class.java, "DeliveryTaskQueue")
    }

    override fun processOrder(orderId: String, amount: Double): OrderResult {
        val saga = Saga(Saga.Options.Builder().build())

        try {
            // 결제 처리
            paymentActivities.processPayment(orderId, amount)
            saga.addCompensation({ paymentActivities.refundPayment(orderId) })

            // 재고 처리
            inventoryActivities.reserveInventory(orderId)
            saga.addCompensation({ inventoryActivities.restockInventory(orderId) })

            // 배송 처리
            deliveryActivities.deliverOrder(orderId)
            saga.addCompensation({ deliveryActivities.cancelDelivery(orderId) })

            return OrderResult(true, "주문 처리 성공")
        } catch (e: Exception) {
            Workflow.getLogger(OrderWorkflowImpl::class.java).warn("Exception occurred, starting compensation", e)
            saga.compensate() // 보상 함수 실행
//            throw Workflow.wrap(e)
            return OrderResult(false, e.message ?: "알 수 없는 오류 발생")
        }
    }

    private fun <T> createActivityStub(activityClass: Class<T>, taskQueue: String): T {
        val activityOptions = ActivityOptions.newBuilder()
            .setTaskQueue(taskQueue)
            .setScheduleToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(
                RetryOptions.newBuilder()
                    .setMaximumAttempts(1) // 재시도 없이 1번만 실행
                    .build()
            )
            .build()
        return Workflow.newActivityStub(activityClass, activityOptions)
    }
}