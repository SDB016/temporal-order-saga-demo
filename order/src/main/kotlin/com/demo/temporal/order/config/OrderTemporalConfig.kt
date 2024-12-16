package com.demo.temporal.order.config

import com.demo.temporal.order.workflow.OrderWorkflow
import com.demo.temporal.order.workflow.OrderWorkflowImpl
import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class OrderTemporalConfig {

    @Bean
    fun workflowServiceStubs(): WorkflowServiceStubs {
        return WorkflowServiceStubs.newServiceStubs(
            WorkflowServiceStubsOptions.newBuilder()
                .setTarget("temporal:7233") // Temporal 서버 주소
                .build()
        )
    }

    @Bean
    fun workflowClient(workflowServiceStubs: WorkflowServiceStubs): WorkflowClient {
        return WorkflowClient.newInstance(workflowServiceStubs)
    }

    @Bean
    fun workerFactory(client: WorkflowClient): WorkerFactory {
        val factory = WorkerFactory.newInstance(client)
        val worker = factory.newWorker("OrderTaskQueue")
        worker.registerWorkflowImplementationTypes(OrderWorkflowImpl::class.java)

        return factory
    }
}
