package com.demo.temporal.order.exception

import com.demo.temporal.order.exception.ErrorCode

class OrderException(
    val errorCode: ErrorCode,
    override val message: String? = null,
    val detail: Map<String, Any> = emptyMap()
) : RuntimeException(message ?: errorCode.message)