package ru.akurbanoff.apptracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.akurbanoff.apptracker.Notifications
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.accessibility.RulesProcessor
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideNotifications(context: Context): Notifications = Notifications(context)


    @Provides
    @Singleton
    fun provideRulesProcessor(appsRepository: AppsRepository): RulesProcessor {
        return RulesProcessor(appsRepository)
    }

    @Provides
    @Singleton
    fun provideAccessibilityEngine(notifications: Notifications, rulesProcessor: RulesProcessor): AccessibilityEngine {
        return AccessibilityEngine(notifications, rulesProcessor)
    }

}
