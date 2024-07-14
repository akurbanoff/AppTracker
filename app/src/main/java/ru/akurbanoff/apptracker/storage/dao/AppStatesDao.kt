package ru.akurbanoff.apptracker.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.akurbanoff.apptracker.storage.dto.AppStateDto

@Dao
interface AppStatesDao {
    @Query("SELECT * from appstatedto")
    suspend fun getAppState(): List<AppStateDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStates(appStateDto: AppStateDto)

    @Delete
    suspend fun deleteStates(appStateDto: AppStateDto)
}
