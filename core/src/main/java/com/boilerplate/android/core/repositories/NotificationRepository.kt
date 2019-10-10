package com.boilerplate.android.core.repositories

import com.boilerplate.android.core.datasources.notifications.NotificationDataSource

class NotificationRepository(
        private val notificationDataSource: NotificationDataSource
) {

    fun showNotification() {
        val notification = notificationDataSource.getDefaultNotificationBuilder().apply {
            setContentTitle("Example")
            setContentText("Some Text")
        }.build()
        notificationDataSource.showNotification(1, notification)
    }
}