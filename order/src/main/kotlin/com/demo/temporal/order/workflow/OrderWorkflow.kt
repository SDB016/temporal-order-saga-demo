package com.demo.temporal.order.workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface OrderWorkflow {
    @WorkflowMethod
    fun processOrder(orderId: String)

}