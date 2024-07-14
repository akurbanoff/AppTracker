package ru.akurbanoff.apptracker.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.akurbanoff.apptracker.storage.dto.RuleDto

@Dao
interface RulesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRule(rule: RuleDto)

    @Delete
    suspend fun deleteRule(rule: RuleDto)
}
