package ru.akurbanoff.apptracker.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.data.repository.AppsRepository

class AppAccessibilityService : AccessibilityService() {

    private var accessibilityEngine: AccessibilityEngine? = null
    private var appsRepository: AppsRepository? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (accessibilityEngine == null) {
            accessibilityEngine = (applicationContext as AppTrackerApplication).accessibilityEngine
        }

        accessibilityEngine?.processEvent(this, event ?: return)
    }

    override fun onInterrupt() {}

}
