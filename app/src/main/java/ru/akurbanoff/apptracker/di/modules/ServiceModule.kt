package ru.akurbanoff.apptracker.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.akurbanoff.apptracker.domain.Notifications
import ru.akurbanoff.apptracker.accessibility.AccessibilityEngine
import ru.akurbanoff.apptracker.accessibility.RulesProcessor
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.data.repository.LinkRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideNotifications(@ApplicationContext context: Context): Notifications = Notifications(context)


    @Provides
    @Singleton
    fun provideRulesProcessor(appsRepository: AppsRepository, linkRepository: LinkRepository): RulesProcessor {
        return RulesProcessor(appsRepository, linkRepository)
    }

    @Provides
    @Singleton
    fun provideAccessibilityEngine(notifications: Notifications, rulesProcessor: RulesProcessor): AccessibilityEngine {
        return AccessibilityEngine(notifications, rulesProcessor)
    }
}
