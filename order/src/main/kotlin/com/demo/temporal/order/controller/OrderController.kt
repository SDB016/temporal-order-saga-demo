package com.demo.temporal.order.controller

import com.demo.temporal.order.common.OrderStatus
import com.demo.temporal.order.exception.ErrorCode
import com.demo.temporal.order.exception.OrderException
import com.demo.temporal.order.model.OrderRequest
import com.demo.temporal.order.model.OrderResponse
import com.demo.temporal.order.workflow.OrderWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowFailedException
import io.temporal.client.WorkflowOptions
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val workflowClient: WorkflowClient
) {

    @PostMapping()
    fun processOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
        val workflowId = "order-workflow-${orderRequest.orderId}"
        val options = WorkflowOptions.newBuilder()
            .setTaskQueue("OrderTaskQueue")
            .setWorkflowId(workflowId)
            .build()

        val workflow = workflowClient.newWorkflowStub(OrderWorkflow::class.java, options)

        return try {
            // Workflow 비동기 실행
            WorkflowClient.start(
                { oId: String, amount: Double ->
                    workflow.processOrder(oId, amount)
                },
                orderRequest.orderId,
                orderRequest.amount
            )
            ResponseEntity.ok(
                OrderResponse(
                    orderId = orderRequest.orderId,
                    workflowId = workflowId,
                    status = OrderStatus.PROCESSING,
                    message = "Order workflow started successfully"
                )
            )
        } catch (e: Exception) {
            throw OrderException(ErrorCode.WORKFLOW_START_FAILED, e.message)
        }
    }

    @GetMapping("/{workflowId}/status")
    fun getOrderStatus(@PathVariable workflowId: String): ResponseEntity<OrderResponse> {
        val status: OrderStatus = try {
            val workflowStub = workflowClient.newUntypedWorkflowStub(workflowId)
            val future = workflowStub.getResultAsync(String::class.java)

            future.takeIf { it.isDone }?.let { OrderStatus.COMPLETED } ?: OrderStatus.PROCESSING
        } catch (e: WorkflowFailedException) {
            OrderStatus.FAILED
        } catch (e: Exception) {
            throw OrderException(ErrorCode.WORKFLOW_STATUS_CHECK_FAILED, e.message)
        }

        return ResponseEntity.ok(
            OrderResponse(
                orderId = getOrderIdFromWorkflowId(workflowId),
                workflowId = workflowId,
                status = status,
                message = "Current workflow status: $status"
            )
        )
    }

    private fun getOrderIdFromWorkflowId(workflowId: String): String {
        return workflowId.removePrefix("order-workflow-")
    }
}