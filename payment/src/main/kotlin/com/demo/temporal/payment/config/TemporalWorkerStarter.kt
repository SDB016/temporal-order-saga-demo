package com.demo.temporal.payment.config

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
            logger.info { "Payment Worker Factory started" }
        } catch (e: Exception) {
            logger.error(e) { "Payment Worker Factory failed" }
            throw e
        }
    }
}