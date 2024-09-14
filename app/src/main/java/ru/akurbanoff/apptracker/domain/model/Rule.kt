package ru.akurbanoff.apptracker.domain.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class Rule(
    open val id: Int,
    open val enabled: Boolean,
    open val packageName: String,
    open val condition: (params: Array<Any>) -> Boolean,
) {
    data class TimeLimitRule(
        override val id: Int,
        override val enabled: Boolean,
        override val packageName: String,
        val limitInSeconds: Int,
    ) : Rule(id, enabled, packageName, condition = { params ->
        val currentLimit = params[0] as Int
        currentLimit < limitInSeconds
    })

    data class HourOfTheDayRangeRule(
        override val id: Int,
        override val enabled: Boolean,
        override val packageName: String,
        val fromHour: Int,
        val fromMinute: Int,
        val toHour: Int,
        val toMinute: Int
    ) : Rule(id, enabled, packageName, condition = { params ->
        val current = params[0] as Int
        (current >= fromHour) && (current <= toHour)
    })
}
