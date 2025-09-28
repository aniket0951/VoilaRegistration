package com.voila.voilasailor.notification.NotificationModel

data class Notification(
    val date: String,
    val id: Int,
    val notification_description: String,
    val notification_title: String,
    val request_token: String
)