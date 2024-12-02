package com.demo.temporal.order.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class TemporalWorkerStarter(private val factory: WorkerFactory) {
    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun startFactory() {
        factory.start()
        logger.info { "Temporal Worker Factory started" }
    }
}