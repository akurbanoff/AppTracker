package ru.akurbanoff.apptracker.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.Notifications
import java.util.Timer

class AccessibilityEngine(context: Context) {

    private val timerTask = Task()

    private var timer: Timer? = null
    private var notifications: Notifications? = null
    private var rulesProcessor: RulesProcessor? = null

    init {
        if (notifications == null) {
            notifications = Notifications(context.applicationContext)
        }

        if (rulesProcessor == null) {
//            val appsRepository = (context as AppTrackerApplication).appsRepository
//            appsRepository?.let { rulesProcessor = RulesProcessor(it) }
        }

        if (timer == null) {
            timer = Timer()
            timer?.schedule(timerTask, 1000)
        }
    }

    fun onDestroy() {
        rulesProcessor = null
        notifications = null
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
        rulesProcessor?.processRules(currentAppPackageName) { service.minimizeCurrentApp() }
    }

    /**
     * Just press device's home button
     */
    private fun AccessibilityService.minimizeCurrentApp() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }

    private inner class Task : java.util.TimerTask() {

        var secondsInApp = 0
        var packageName: String? = null
            set(value) {
                if (packageName != value) {
                    rulesProcessor?.registerInAppTime(packageName ?: return, secondsInApp * ONE_SECOND)
                    secondsInApp = 0
                }

                field = value
            }

        override fun run() {
            secondsInApp += 1
        }
    }

    private companion object {
        private const val ONE_SECOND = 1000
    }
}
