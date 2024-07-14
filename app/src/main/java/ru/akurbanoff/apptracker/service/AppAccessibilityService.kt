package ru.akurbanoff.apptracker.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine

class AppAccessibilityService : AccessibilityService() {

    private var accessibilityEngine: AccessibilityEngine? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (accessibilityEngine == null) {
            accessibilityEngine = AccessibilityEngine(applicationContext)
        }

        accessibilityEngine?.processEvent(this, event ?: return)
    }

    override fun onDestroy() {
        super.onDestroy()
        accessibilityEngine?.onDestroy()
        accessibilityEngine = null
    }

    override fun onInterrupt() {}

}
