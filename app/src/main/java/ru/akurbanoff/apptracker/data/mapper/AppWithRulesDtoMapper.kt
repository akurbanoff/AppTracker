package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.storage.dto.AppWithRulesDto
import javax.inject.Inject

class AppWithRulesDtoMapper @Inject constructor(
    private val rulesRuleDtoRuleMapper: RuleDtoRuleMapper,
    private val appDtoAppMapper: AppDtoAppMapper,
) {
    operator fun invoke(appWithRules: AppWithRulesDto): AppWithRules = AppWithRules(
        app = appDtoAppMapper.invoke(appWithRules.app),
        rules = appWithRules.posts.map { rulesRuleDtoRuleMapper.invoke(it) }
    )
}
