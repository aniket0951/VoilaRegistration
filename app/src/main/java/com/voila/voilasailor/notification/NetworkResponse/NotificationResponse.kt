package com.voila.voilasailor.notification.NetworkResponse

import com.voila.voilasailor.notification.NotificationModel.Notification

data class NotificationResponse(
    val message: String,
    val notifications: List<Notification>,
    val result: Boolean
)