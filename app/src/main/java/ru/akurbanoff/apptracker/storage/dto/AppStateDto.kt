package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppStateDto(
    @PrimaryKey
    val packageName: String,
    val timeInApp: Int,
)
