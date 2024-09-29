package ru.akurbanoff.apptracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.akurbanoff.apptracker.storage.dto.AppDto
import ru.akurbanoff.apptracker.storage.dto.LinkDto

@Dao
interface LinkDao {
    @Query("SELECT * FROM linkdto")
    fun getAll(): Flow<List<LinkDto>>

    @Query("SELECT * FROM linkdto")
    fun getList(): List<LinkDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(link: LinkDto)

    @Query("DELETE FROM linkdto WHERE link = :link")
    suspend fun delete(link: String)

    @Query("UPDATE linkdto SET enabled = :enabled WHERE link = :link")
    suspend fun checkApp(link: String, enabled: Boolean)

}