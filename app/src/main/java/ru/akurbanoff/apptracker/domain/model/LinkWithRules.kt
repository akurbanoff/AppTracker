package ru.akurbanoff.apptracker.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class LinkWithRules (
    val link: Link,
    val rules: List<Rule>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LinkWithRules

        return link == other.link
    }

    override fun hashCode(): Int {
        return link.hashCode()
    }
}