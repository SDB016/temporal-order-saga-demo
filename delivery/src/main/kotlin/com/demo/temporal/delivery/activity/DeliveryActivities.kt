package com.demo.temporal.delivery.activity

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface DeliveryActivities {
    fun cancelDelivery(orderId: String)
    fun deliverOrder(orderId: String)

}