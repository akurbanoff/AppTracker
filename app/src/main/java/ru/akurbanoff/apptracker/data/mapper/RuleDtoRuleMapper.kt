package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.storage.dto.RuleDto
import ru.akurbanoff.apptracker.storage.dto.RuleType
import javax.inject.Inject

class RuleDtoRuleMapper @Inject constructor() {
    operator fun invoke(rule: RuleDto): Rule {
        when (rule.type) {
            RuleType.TIME_LIMIT_RULE -> {
                return Rule.TimeLimitRule(
                    id = rule.id,
                    enabled = rule.enabled,
                    packageName = rule.packageName,
                    limitInSeconds = rule.params[LIMIT_IN_SECONDS].toString().toInt(),
                )
            }

            RuleType.HOUR_OF_THE_DAY_RANGE_RULE -> {
                return Rule.HourOfTheDayRangeRule(
                    id = rule.id,
                    enabled = rule.enabled,
                    packageName = rule.packageName,
                    fromHour = rule.params[FROM_HOUR].toString().toInt(),
                    fromMinute = rule.params[FROM_MINUTE].toString().toInt(),
                    toHour = rule.params[TO_HOUR].toString().toInt(),
                    toMinute = rule.params[TO_MINUTE].toString().toInt(),
                )
            }
        }
    }

     companion object {
        private const val LIMIT_IN_SECONDS = "limitInSeconds"
        const val FROM_HOUR = "from_hour"
        const val TO_HOUR = "to_hour"
        const val FROM_MINUTE = "from_minute"
        const val TO_MINUTE = "to_minute"
    }
}
