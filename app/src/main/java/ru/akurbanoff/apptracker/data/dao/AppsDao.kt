package ru.akurbanoff.apptracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.akurbanoff.apptracker.data.model.App
import ru.akurbanoff.apptracker.data.model.AppWithRules
import ru.akurbanoff.apptracker.data.model.rule.Rule

@Dao
interface AppsDao {
    @Transaction
    @Query("SELECT * FROM app")
    suspend fun getAppsWithRules(): List<AppWithRules>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateApp(app: App)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRule(rule: Rule)

    @Query("DELETE FROM rule WHERE applicationId = :appId")
    suspend fun deleteRulesByAppId(appId: Int)
}
