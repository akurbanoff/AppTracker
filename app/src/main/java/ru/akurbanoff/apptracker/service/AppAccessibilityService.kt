package ru.akurbanoff.apptracker.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.di.getApplicationComponent
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.Rule

class AppAccessibilityService : AccessibilityService() {

    private var accessibilityEngine: AccessibilityEngine? = null
    private var appsRepository: AppsRepository? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (accessibilityEngine == null) {
            accessibilityEngine = getApplicationComponent(applicationContext).accessibilityEngine
        }
        if (appsRepository == null) {
            appsRepository = getApplicationComponent(applicationContext).appsRepository
        }
        accessibilityEngine?.processEvent(this, event ?: return)

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                appsRepository?.updateApp(
                    App(
                        id = 0,
                        packageName = "com.android.settings",
                        enabled = true
                    )
                )

                appsRepository?.getAllApps()?.forEach {
                    appsRepository?.addRule(
                        Rule.TimeLimitRule(
                            id = 0,
                            enabled = true,
                            applicationId = it.app.id,
                            limitInSeconds = 30,
                        )
                    )
                }
            }
        }
    }

    override fun onInterrupt() {}

}
