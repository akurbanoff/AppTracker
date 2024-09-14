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
                    fromHour = rule.params[FROM].toString().toInt(),
                    fromMinute = 0,
                    toHour = rule.params[TO].toString().toInt(),
                    toMinute = 0
                )
            }
        }
    }

    private companion object {
        private const val LIMIT_IN_SECONDS = "limitInSeconds"
        private const val FROM = "from"
        private const val TO = "to"
    }
}
