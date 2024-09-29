package ru.akurbanoff.apptracker.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.domain.Notifications
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
            timer?.schedule(timerTask, 0, 1000)
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
        val currentLink = source.text.toString()

        timerTask.link = currentLink
        timerTask.packageName = currentAppPackageName
        rulesProcessor.processRules(currentAppPackageName) {
            notifications.displayNotification(
                R.string.notification_limit_reached_title,
                R.string.notification_limit_reached_message,
                R.drawable.ic_launcher_background
            )
            service.minimizeCurrentApp()
        }

        rulesProcessor.processLinkRules(currentLink){
            // TODO: Определиться как мы будем работать со ссылками и добавить нотификацию
            service.minimizeCurrentLink()
        }
    }

    /**
     * Just press device's home button
     */
    private fun AccessibilityService.minimizeCurrentApp() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }

    private fun AccessibilityService.minimizeCurrentLink() {
        // TODO: Добавить событие на закрытие ссылки
    }

    private inner class Task : java.util.TimerTask() {

        @Volatile
        var secondsInApp = 0

        @Volatile
        var secondsInLink = 0

        var packageName: String? = null
            set(value) {
                onPackageNameUpdate(value)
                field = value
            }

        var link: String? = null
            set(value) {
                onLinkUpdate(value)
                field = value
            }

        override fun run() {
            secondsInApp += 1
            secondsInLink += 1
            updateTime()
        }

        private fun onPackageNameUpdate(newValue: String?) {
            if (packageName != newValue) {
                updateTime()
                secondsInApp = 0
            }
        }

        private fun onLinkUpdate(newValue: String?) {
            if(link != newValue){
                updateTime()
                secondsInLink = 0
            }
        }
    }

    private fun Task.updateTime() {
        rulesProcessor.registerInAppTime(packageName ?: link ?: return, secondsInApp)
    }
}
