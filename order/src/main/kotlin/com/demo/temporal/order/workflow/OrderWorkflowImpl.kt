package com.demo.temporal.order.workflow

import com.demo.temporal.order.activities.DeliveryActivities
import com.demo.temporal.order.activities.InventoryActivities
import com.demo.temporal.order.activities.PaymentActivities
import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Saga
import io.temporal.workflow.Workflow
import java.time.Duration

class OrderWorkflowImpl : OrderWorkflow {
    private val paymentOptions: ActivityOptions = ActivityOptions.newBuilder()
        .setTaskQueue("PaymentTaskQueue")
        .setScheduleToCloseTimeout(Duration.ofSeconds(30))
        .build()

    private val inventoryOptions: ActivityOptions = ActivityOptions.newBuilder()
        .setTaskQueue("InventoryTaskQueue")
        .setScheduleToCloseTimeout(Duration.ofSeconds(30))
        .build()

    private val deliveryOptions: ActivityOptions = ActivityOptions.newBuilder()
        .setTaskQueue("DeliveryTaskQueue")
        .setScheduleToCloseTimeout(Duration.ofSeconds(30))
        .build()

    private val paymentActivities = Workflow.newActivityStub(PaymentActivities::class.java, paymentOptions)
    private val inventoryActivities = Workflow.newActivityStub(InventoryActivities::class.java, inventoryOptions)
    private val deliveryActivities = Workflow.newActivityStub(DeliveryActivities::class.java, deliveryOptions)


    override fun processOrder(orderId: String) {
        val saga = Saga(Saga.Options.Builder().build())

        try {
            // 결제 처리
            saga.addCompensation { paymentActivities.refundPayment(orderId) }
            paymentActivities.processPayment(orderId)

            // 재고 예약
            saga.addCompensation { inventoryActivities.restockInventory(orderId) }
            inventoryActivities.reserveInventory(orderId)

            // 배송 처리
            saga.addCompensation { deliveryActivities.cancelDelivery(orderId) }
            deliveryActivities.deliverOrder(orderId)
        } catch (e: Exception) {
            saga.compensate()
            throw Workflow.wrap(e)
        }
    }

}