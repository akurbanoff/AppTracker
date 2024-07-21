package ru.akurbanoff.apptracker

import android.support.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import ru.akurbanoff.apptracker.di.AppComponent
import ru.akurbanoff.apptracker.di.DaggerAppComponent

@HiltAndroidApp
class AppTrackerApplication : MultiDexApplication() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
    }
}
