package ru.akurbanoff.apptracker.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.Notifications
import ru.akurbanoff.apptracker.R
import java.util.Timer
import javax.inject.Inject

class AccessibilityEngine @Inject constructor(
    private val notifications: Notifications,
    private val rulesProcessor: RulesProcessor,
) {

    private val timerTask = Task()
    private var timer: Timer? = null

    init {
        if (timer == null) {
            timer = Timer()
            timer?.schedule(timerTask, 1000)
        }
    }

    fun onDestroy() {
        timer?.cancel()
        timer = null
    }

    /**
     * Processes an accessibility event received by the provided AccessibilityService.
     *
     * @param event The AccessibilityEvent that was received.
     */
    fun processEvent(service: AccessibilityService, event: AccessibilityEvent) {
        val source = event.source ?: return
        val currentAppPackageName = source.packageName.toString()

        timerTask.packageName = currentAppPackageName
        rulesProcessor.processRules(currentAppPackageName) {
            notifications.displayNotification(
                R.string.notification_limit_reached_title,
                R.string.notification_limit_reached_message,
                R.drawable.ic_launcher_background
            )
            service.minimizeCurrentApp()
        }
    }

    /**
     * Just press device's home button
     */
    private fun AccessibilityService.minimizeCurrentApp() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }

    private inner class Task : java.util.TimerTask() {

        @Volatile
        var secondsInApp = 0
        var packageName: String? = null
            set(value) {
                onPackageNameUpdate(value)
                field = value
            }

        override fun run() {
            secondsInApp += 1
        }

        private fun onPackageNameUpdate(newValue: String?) {
            if (packageName != newValue) {
                rulesProcessor.registerInAppTime(newValue ?: return, secondsInApp * ONE_SECOND)
                secondsInApp = 0
            }
        }
    }

    private companion object {
        private const val ONE_SECOND = 1000
    }
}
