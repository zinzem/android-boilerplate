package  com.boilerplate.android.core.datasources.notifications

import android.app.*
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.boilerplate.android.core.R

class NotificationDataSource(
        private val context: Context
) {

    private val notificationManager: NotificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !mainNotificationChannelExists(context)) {
            createMainNotificationChannel(context)
        }
    }

    fun showNotification(notificationId: Int, notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    fun getDefaultNotificationBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, NotificationDataSource.CHANNEL_ID)
                    //.setSmallIcon(R.drawable.)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(longArrayOf(0, 100))
                    .setOnlyAlertOnce(true)
        } else {
            NotificationCompat.Builder(context)
                    //.setSmallIcon(R.drawable.)
                    //.setColor(context.resources.getColor(R.color.red))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMainNotificationChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        channel.description = CHANNEL_DESCRIPTION
        channel.enableLights(true)
        channel.lightColor = context.resources.getColor(R.color.colorAccent)
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mainNotificationChannelExists(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.getNotificationChannel(CHANNEL_ID) != null
    }

    companion object {

        const val CHANNEL_ID = "SHOPI"

        private const val CHANNEL_NAME = "Shopi"
        private const val CHANNEL_DESCRIPTION = "N'oublie plus jamais test courses"
    }

}