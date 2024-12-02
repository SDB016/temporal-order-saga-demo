package com.demo.temporal.order.controller

import com.demo.temporal.order.workflow.OrderWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val workflowClient: WorkflowClient
) {

    @PostMapping("/{orderId}")
    fun processOrder(@PathVariable orderId: String): ResponseEntity<String> {
        val options = WorkflowOptions.newBuilder()
            .setTaskQueue("OrderTaskQueue")
            .setWorkflowId("order-workflow-$orderId")
            .build()

        val workflow = workflowClient.newWorkflowStub(OrderWorkflow::class.java, options)
        WorkflowClient.start({ orderId: String -> workflow.processOrder(orderId) }, orderId)
//        workflow.processOrder(orderId)
        return ResponseEntity.ok("Order workflow started for orderId: $orderId")
    }
}