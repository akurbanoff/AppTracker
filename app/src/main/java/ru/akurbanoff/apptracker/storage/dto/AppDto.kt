package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id", "packageName"])
data class AppDto(
    val id: Int,
    val packageName: String,
    val enabled: Boolean,
)
