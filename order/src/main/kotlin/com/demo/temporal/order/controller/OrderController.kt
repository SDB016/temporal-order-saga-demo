package com.demo.temporal.order.controller

import com.demo.temporal.order.workflow.OrderWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val workflowClient: WorkflowClient
) {

    @PostMapping("/orders")
    fun processOrder(@RequestParam orderId: String) {
        val options = WorkflowOptions.newBuilder()
            .setTaskQueue("OrderTaskQueue")
            .setWorkflowId("order-workflow-$orderId")
            .build()

        val workflow = workflowClient.newWorkflowStub(OrderWorkflow::class.java, options)
        workflow.processOrder(orderId)
    }
}