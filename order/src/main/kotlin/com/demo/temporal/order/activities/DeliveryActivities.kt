package com.demo.temporal.order.activities

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface DeliveryActivities {
    fun cancelDelivery(orderId: String)
    fun deliverOrder(orderId: String)

}
