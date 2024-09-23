package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LinkDto(
    @PrimaryKey
    val link: String,
    val title: String
)