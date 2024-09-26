package ru.akurbanoff.apptracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val title: String,
    val link: String,
    val enabled: Boolean,
)