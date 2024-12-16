package com.demo.temporal.delivery.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class TemporalWorkerStarter(private val factory: WorkerFactory) {
    private val logger = KotlinLogging.logger {  }

    @PostConstruct
    fun startFactory() {
        try {
            factory.start()
            logger.info { "Delivery Worker Factory started" }
        } catch (e: Exception) {
            logger.error(e) { "Delivery Worker Factory failed" }
            throw e
        }
    }
}