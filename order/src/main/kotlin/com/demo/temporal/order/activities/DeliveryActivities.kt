package com.demo.temporal.order.activities

interface DeliveryActivities {
    fun cancelDelivery(orderId: String)
    fun deliverOrder(orderId: String)

}
