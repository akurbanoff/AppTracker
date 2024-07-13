package ru.akurbanoff.apptracker.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine

class AppAccessibilityService : AccessibilityService() {

    // todo inject via DI
    private val accessibilityEngine = AccessibilityEngine()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        accessibilityEngine.processEvent(this, event ?: return)
    }

    override fun onInterrupt() {}

}
