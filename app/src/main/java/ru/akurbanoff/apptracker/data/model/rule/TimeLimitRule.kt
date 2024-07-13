package ru.akurbanoff.apptracker.data.model.rule

import androidx.room.Entity

@Entity
data class TimeLimitRule(
    override var id: Int,
    override val enabled: Boolean,
    override val applicationId: Int,
    val limitInSeconds: Int,
) : Rule()
