package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.storage.dto.RuleDto
import ru.akurbanoff.apptracker.storage.dto.RuleType
import javax.inject.Inject

class RuleRuleDtoMapper @Inject constructor() {
    operator fun invoke(rule: Rule): RuleDto {
        val params = hashMapOf<String, Any>()
        val type: RuleType = when (rule) {
            is Rule.TimeLimitRule -> {
                params[LIMIT_IN_SECONDS] = rule.limitInSeconds
                RuleType.TIME_LIMIT_RULE
            }

            is Rule.HourOfTheDayRangeRule -> {
                params[FROM] = rule.fromHour
                params[TO] = rule.toHour
                RuleType.HOUR_OF_THE_DAY_RANGE_RULE
            }
        }
        return RuleDto(
            id = rule.id,
            enabled = rule.enabled,
            packageName = rule.packageName,
            params = params,
            type = type
        )
    }

    private companion object {
        private const val LIMIT_IN_SECONDS = "limitInSeconds"
        private const val FROM = "from"
        private const val TO = "to"
    }
}
