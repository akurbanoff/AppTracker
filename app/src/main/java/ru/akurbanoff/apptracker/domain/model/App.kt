package ru.akurbanoff.apptracker.domain.model

data class App(
    val id: Int,
    val packageName: String,
    var enabled: Boolean,
)
