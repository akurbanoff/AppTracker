package ru.akurbanoff.apptracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class Notifications(private val context: Context) {

    private var random = Random(System.currentTimeMillis())

    init {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Displays a one-time notification with the specified title, message, and icon.
     *
     * @param title The title of the notification. This should be a non-null, non-empty string.
     * @param message The message content of the notification. This should be a non-null, non-empty string.
     * @param icon The resource ID of the small icon to be used in the notification. This should be a valid drawable resource.
     */
    fun displayNotification(title: String, message: String, @DrawableRes icon: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return notificationManager.notify(random.nextInt(), builder.build())
    }

    /**
     * Displays a one-time notification with the specified title, message, and icon, where the title and message are retrieved from string resources.
     *
     * @param title The resource ID of the title string to be used in the notification.
     * @param message The resource ID of the message string to be used in the notification.
     * @param icon The resource ID of the small icon to be used in the notification. This should be a valid drawable resource.
     */
    fun displayNotification(@StringRes title: Int, @StringRes message: Int, @DrawableRes icon: Int) {
        displayNotification(context.getString(title), context.getString(message), icon)
    }


    private companion object {
        const val CHANNEL_ID = "app_tracker"
        const val CHANNEL_NAME = "App Tracker Notification Channel"
    }
}
