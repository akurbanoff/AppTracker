package ru.akurbanoff.apptracker.domain.model

data class AppState(
    val packageName: String,
    val timeInApp: Int,
)
