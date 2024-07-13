package ru.akurbanoff.apptracker.data.model

data class ApplicationPreference(
    val packageName: String,
    var enabled: Boolean,
)
