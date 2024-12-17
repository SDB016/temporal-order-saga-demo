package com.demo.temporal.order.workflow

import com.demo.temporal.delivery.activity.DeliveryActivities
import com.demo.temporal.inventory.activity.InventoryActivities
import com.demo.temporal.payment.activity.PaymentActivities
import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Saga
import io.temporal.workflow.Workflow
import java.time.Duration
import org.springframework.stereotype.Component

@Component
class OrderWorkflowImpl : OrderWorkflow {

    private val paymentActivities = createActivityStub(PaymentActivities::class.java, "PaymentTaskQueue")
    private val inventoryActivities = createActivityStub(InventoryActivities::class.java, "InventoryTaskQueue")
    private val deliveryActivities = createActivityStub(DeliveryActivities::class.java, "DeliveryTaskQueue")

    override fun processOrder(orderId: String, amount: Double) {
        val saga = Saga(Saga.Options.Builder().build())

        // Workflow 구성
        try {
            // 결제 처리
            processPayment(saga, orderId, amount)
            // 재고 처리
            reserveInventory(saga, orderId)
            // 배송 처리
            processDelivery(saga, orderId)
        } catch (e: Exception) {
            saga.compensate()
            throw Workflow.wrap(e)
        }
    }

    // 각 처리 단계
    private fun processPayment(saga: Saga, orderId: String, amount: Double) {
        saga.addCompensation { paymentActivities.refundPayment(orderId) }
        paymentActivities.processPayment(orderId, amount)
    }

    private fun reserveInventory(saga: Saga, orderId: String) {
        saga.addCompensation { inventoryActivities.restockInventory(orderId) }
        inventoryActivities.reserveInventory(orderId)
    }

    private fun processDelivery(saga: Saga, orderId: String) {
        saga.addCompensation { deliveryActivities.cancelDelivery(orderId) }
        deliveryActivities.deliverOrder(orderId)
    }


    private fun <T> createActivityStub(activityClass: Class<T>, taskQueue: String): T {
        val activityOptions = ActivityOptions.newBuilder()
            .setTaskQueue(taskQueue)
            .setScheduleToCloseTimeout(Duration.ofSeconds(30))
            .build()
        return Workflow.newActivityStub(activityClass, activityOptions)
    }
}