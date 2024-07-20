package ru.akurbanoff.apptracker.domain.model

import android.graphics.Bitmap

data class App(
    val id: Int,
    val name: String? = null,
    val icon: Bitmap? = null,
    val packageName: String,
    var enabled: Boolean,
)
