package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RuleDto(
    @PrimaryKey
    val id: Int = 0,
    val type: RuleType,
    val enabled: Boolean = false,
    val applicationId: Int = 0,
    val params: Map<String, Any>,
)

enum class RuleType {
    HOUR_OF_THE_DAY_RANGE_RULE, TIME_LIMIT_RULE
}
