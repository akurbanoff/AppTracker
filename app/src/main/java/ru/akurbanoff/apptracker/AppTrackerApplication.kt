package ru.akurbanoff.apptracker

import android.app.Application
import android.os.Looper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.di.AppComponent
import ru.akurbanoff.apptracker.di.DaggerAppComponent
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.Rule
import javax.inject.Inject

private const val s = "com.google.android.dialer"

@HiltAndroidApp
class AppTrackerApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
    }

//    @Inject
//    var appsRepository: AppsRepository? = null
//
//    override fun onCreate() {
//        super.onCreate()
//
//        android.os.Handler(Looper.getMainLooper()).postDelayed({
//            CoroutineScope(Dispatchers.IO).launch {
//                appsRepository?.updateApp(
//                    App(
//                        id = s.hashCode(),
//                        packageName = s,
//                        enabled = true,
//                    )
//                )
//                appsRepository?.addRule(
//                    Rule.TimeLimitRule(
//                        id = 0,
//                        enabled = true,
//                        applicationId = s.hashCode(),
//                        150
//                    )
//                )
//            }
//        }, 3000)
//    }
}
