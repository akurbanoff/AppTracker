package ru.akurbanoff.apptracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.akurbanoff.apptracker.storage.dto.AppDto

@Dao
interface AppsDao {
    @Query("SELECT * FROM `AppDto`")
    fun getAll(): Flow<List<AppDto>>

    @Query("SELECT * FROM appdto")
    fun getList(): List<AppDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(app: AppDto)

    @Query("DELETE FROM appdto WHERE packageName = :packageName")
    suspend fun delete(packageName: String)
}
