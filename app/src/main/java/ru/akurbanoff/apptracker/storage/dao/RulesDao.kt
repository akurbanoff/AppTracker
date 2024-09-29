package ru.akurbanoff.apptracker.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.akurbanoff.apptracker.storage.dto.RuleDto

@Dao
interface RulesDao {
    @Query("SELECT * FROM `ruledto` WHERE packageName = :packageName")
    suspend fun getRulesByPackage(packageName: String): List<RuleDto>

    @Query("SELECT * FROM `ruledto` WHERE link = :link")
    suspend fun getRulesByLink(link: String): List<RuleDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRule(rule: RuleDto)

    @Delete
    suspend fun deleteRule(rule: RuleDto)
}
