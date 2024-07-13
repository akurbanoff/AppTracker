package ru.akurbanoff.apptracker.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.Notifications
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.data.model.ApplicationPreference

class AccessibilityEngine {

    private var notifications: Notifications? = null

    val appPreferences = listOf(
        ApplicationPreference(packageName = "com.google.android.dialer", enabled = true)
    )

    /**
     * Processes an accessibility event received by the provided AccessibilityService.
     *
     * @param service The AccessibilityService that received the event.
     * @param event The AccessibilityEvent that was received.
     */
    fun processEvent(service: AccessibilityService, event: AccessibilityEvent) {
        // здесь обработаем события, приходящие от AccessibilityService
        // нам нужно понять, какое приложение в данный момент используется
        // логику обработки правил потом перенесем отсюда

        val source = event.source ?: return
        val currentAppPackageName = source.packageName.toString()

        for (app in appPreferences) {
            if (app.packageName == currentAppPackageName && app.enabled) {
                if (notifications == null) notifications = Notifications(service.applicationContext)
                notifications?.displayNotification(
                    R.string.notification_limit_reached_title,
                    R.string.notification_limit_reached_message,
                    R.drawable.ic_launcher_background
                )
                service.minimizeCurrentApp()
            }
        }
    }

    /**
     * Just press device's home button
     */
    private fun AccessibilityService.minimizeCurrentApp() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }
}
