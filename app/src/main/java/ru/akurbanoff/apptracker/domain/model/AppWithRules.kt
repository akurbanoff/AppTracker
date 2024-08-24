package ru.akurbanoff.apptracker.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AppWithRules(
    val app: App,
    val rules: List<Rule>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppWithRules

        return app == other.app
    }

    override fun hashCode(): Int {
        return app.hashCode()
    }
}
