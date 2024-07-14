package ru.akurbanoff.apptracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.storage.dto.AppDto
import ru.akurbanoff.apptracker.storage.dto.AppWithRulesDto
import ru.akurbanoff.apptracker.storage.dto.RuleDto

@Dao
interface AppsDao {
    @Transaction
    @Query("SELECT * FROM appdto")
    suspend fun getAppsWithRules(): List<AppWithRulesDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateApp(app: AppDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRule(rule: RuleDto)

    @Query("DELETE FROM ruledto WHERE applicationId = :appId")
    suspend fun deleteRulesByAppId(appId: Int)
}
