package ru.akurbanoff.apptracker.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine

class AppAccessibilityService : AccessibilityService() {

    // todo inject via DI
    private val accessibilityEngine = AccessibilityEngine()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        accessibilityEngine.processEvent(this, event ?: return)

        GlobalScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

    override fun onInterrupt() {}

}
