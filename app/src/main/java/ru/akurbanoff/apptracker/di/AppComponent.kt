package ru.akurbanoff.apptracker.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
        ServiceModule::class,
    ]
)
@Singleton
interface AppComponent {

    val accessibilityEngine: AccessibilityEngine
    val appsRepository: AppsRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}

fun getApplicationComponent(context: Context): AppComponent {
    return (context.applicationContext as AppTrackerApplication).appComponent
}
