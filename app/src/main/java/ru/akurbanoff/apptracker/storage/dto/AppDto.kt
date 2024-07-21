package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppDto(
    @PrimaryKey
    val packageName: String,
    val enabled: Boolean,
)
