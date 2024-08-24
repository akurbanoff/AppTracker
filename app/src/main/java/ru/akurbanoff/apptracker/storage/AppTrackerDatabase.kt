package ru.akurbanoff.apptracker.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.akurbanoff.apptracker.storage.dao.AppStatesDao
import ru.akurbanoff.apptracker.storage.dao.AppsDao
import ru.akurbanoff.apptracker.storage.dao.RulesDao
import ru.akurbanoff.apptracker.storage.dto.AppDto
import ru.akurbanoff.apptracker.storage.dto.AppStateDto
import ru.akurbanoff.apptracker.storage.dto.RuleDto

@Database(entities = [AppDto::class, AppStateDto::class, RuleDto::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppTrackerDatabase : RoomDatabase() {
    abstract fun appsDao(): AppsDao
    abstract fun rulesDao(): RulesDao
    abstract fun appStatsDao(): AppStatesDao
}
