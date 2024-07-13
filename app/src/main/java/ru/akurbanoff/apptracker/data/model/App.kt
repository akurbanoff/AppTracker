package ru.akurbanoff.apptracker.data.model

import androidx.room.Entity

@Entity
data class App(
    val packageName: String,
    var enabled: Boolean,
)
