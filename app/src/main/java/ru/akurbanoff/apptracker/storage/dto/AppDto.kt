package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppDto(
    @PrimaryKey
    val id: Int,
    val packageName: String,
    var enabled: Boolean,
)
