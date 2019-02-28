package com.boilerplate.android.core.repositories

import com.boilerplate.android.core.datasources.notifications.NotificationDataSource


class NotificationRepository(
        private val notificationDataSource: NotificationDataSource
) {

    fun showEnterGeofenceNotification() {
        val notification = notificationDataSource.getDefaultNotificationBuilder().apply {
            setContentTitle("Shopi")
            setContentText("You just entered into a Geofence")
        }.build()
        notificationDataSource.showNotification(1, notification)
    }

    fun showDwellGeofenceNotification() {
        val notification = notificationDataSource.getDefaultNotificationBuilder().apply {
            setContentTitle("Shopi")
            setContentText("You are dwelling in a Geofence")
        }.build()
        notificationDataSource.showNotification(1, notification)
    }

    fun showExitGeofenceNotification() {
        val notification = notificationDataSource.getDefaultNotificationBuilder().apply {
            setContentTitle("Shopi")
            setContentText("You just exited a Geofence")
        }.build()
        notificationDataSource.showNotification(1, notification)
    }
}