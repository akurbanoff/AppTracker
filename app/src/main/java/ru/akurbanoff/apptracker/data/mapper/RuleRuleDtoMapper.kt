package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.data.mapper.RuleDtoRuleMapper.Companion.FROM_HOUR
import ru.akurbanoff.apptracker.data.mapper.RuleDtoRuleMapper.Companion.FROM_MINUTE
import ru.akurbanoff.apptracker.data.mapper.RuleDtoRuleMapper.Companion.TO_HOUR
import ru.akurbanoff.apptracker.data.mapper.RuleDtoRuleMapper.Companion.TO_MINUTE
import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.storage.dto.RuleDto
import ru.akurbanoff.apptracker.storage.dto.RuleType
import java.util.Calendar
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
                params[FROM_HOUR] = rule.fromHour
                params[TO_HOUR] = rule.toHour
                params[FROM_MINUTE] = rule.fromMinute
                params[TO_MINUTE] = rule.toMinute
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
