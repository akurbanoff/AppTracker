package ru.akurbanoff.apptracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.akurbanoff.apptracker.data.model.App
import ru.akurbanoff.apptracker.data.model.AppWithRules
import ru.akurbanoff.apptracker.data.model.rule.Rule

@Dao
interface RulesDao {
    @Delete
    suspend fun insertOrUpdateRule(rule: Rule)
}
