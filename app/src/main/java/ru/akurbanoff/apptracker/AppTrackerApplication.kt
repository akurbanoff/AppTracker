package ru.akurbanoff.apptracker

import android.os.Looper
import android.support.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.di.AppComponent
import ru.akurbanoff.apptracker.di.DaggerAppComponent
import ru.akurbanoff.apptracker.domain.model.Rule
import javax.inject.Inject

@HiltAndroidApp
class AppTrackerApplication : MultiDexApplication() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var accessibilityEngine: AccessibilityEngine

    @Inject
    lateinit var appsRepository: AppsRepository

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            CoroutineScope(Dispatchers.IO).launch {
                appsRepository.addRule(
                    Rule.TimeLimitRule(
                        id = 5,
                        enabled = true,
                        packageName = "ru.dostaevsky.android",
                        limitInSeconds = 10
                    )
                )
            }
        }, 3000)
    }
}
