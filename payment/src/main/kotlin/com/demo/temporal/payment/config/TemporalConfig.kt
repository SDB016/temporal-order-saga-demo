package com.demo.temporal.payment.config

import com.demo.temporal.payment.activity.PaymentActivitiesImpl
import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import io.temporal.workflow.Workflow
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalConfig(
    @Value("\${temporal.server}") private val server: String,
    @Value("\${temporal.task-queue}") private val taskQueue: String
) {

    @Bean
    fun workflowServiceStubs(): WorkflowServiceStubs {
        return WorkflowServiceStubs.newServiceStubs(
            WorkflowServiceStubsOptions.newBuilder()
                .setTarget(server)
                .build()
        )
    }

    @Bean
    fun workflowClient(service: WorkflowServiceStubs): WorkflowClient {
        return WorkflowClient.newInstance(service)
    }

    @Bean
    fun workerFactory(client: WorkflowClient): WorkerFactory {
        return WorkerFactory.newInstance(client)
    }

    @Bean
    fun paymentWorker(factory: WorkerFactory): Worker {
        val worker = factory.newWorker(taskQueue)
        worker.registerActivitiesImplementations(PaymentActivitiesImpl())
        return worker
    }

}