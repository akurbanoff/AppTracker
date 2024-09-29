package ru.akurbanoff.apptracker.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.di.modules.ServiceModule
import ru.akurbanoff.apptracker.di.modules.DatabaseModule
import ru.akurbanoff.apptracker.domain.Notifications
import ru.akurbanoff.apptracker.ui.app_list.AppListViewModel
import ru.akurbanoff.apptracker.ui.service_settings.ServiceSettingsViewModel
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
        ServiceModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}
