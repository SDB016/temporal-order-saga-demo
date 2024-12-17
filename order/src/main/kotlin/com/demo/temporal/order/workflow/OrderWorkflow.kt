package com.demo.temporal.order.workflow

import com.demo.temporal.order.common.model.OrderResult
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface OrderWorkflow {
    @WorkflowMethod
    fun processOrder(orderId: String, amount: Double): OrderResult

}