package ru.akurbanoff.apptracker.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.akurbanoff.apptracker.storage.AppTrackerDatabase
import ru.akurbanoff.apptracker.storage.dao.AppStatesDao
import ru.akurbanoff.apptracker.storage.dao.AppsDao
import ru.akurbanoff.apptracker.storage.dao.RulesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppTrackerDatabase =
        Room.databaseBuilder(context, AppTrackerDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideAppsDao(database: AppTrackerDatabase): AppsDao = database.appsDao()

    @Provides
    @Singleton
    fun provideRulesDao(database: AppTrackerDatabase): RulesDao = database.rulesDao()

    @Provides
    @Singleton
    fun provideAppStatsDao(database: AppTrackerDatabase): AppStatesDao = database.appStatsDao()


    private companion object {
        private const val DB_NAME = "database"
    }

}
