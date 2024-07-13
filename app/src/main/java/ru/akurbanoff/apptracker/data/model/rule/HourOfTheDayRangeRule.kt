package ru.akurbanoff.apptracker.data.model.rule

import androidx.room.Entity

@Entity
data class HourOfTheDayRangeRule(
    override var id: Int,
    override val enabled: Boolean,
    override val applicationId: Int,
    val from: Int,
    val to: Int,
) : Rule()
