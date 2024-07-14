package ru.akurbanoff.apptracker.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.akurbanoff.apptracker.AppTrackerApplication
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}

fun getApplicationComponent(context: Context): AppComponent{
    return (context.applicationContext as AppTrackerApplication).appComponent
}