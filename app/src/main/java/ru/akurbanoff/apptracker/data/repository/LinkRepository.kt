package ru.akurbanoff.apptracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.akurbanoff.apptracker.data.mapper.LinkDtoToLinkMapper
import ru.akurbanoff.apptracker.data.mapper.LinkToDtoMapper
import ru.akurbanoff.apptracker.data.mapper.RuleDtoRuleMapper
import ru.akurbanoff.apptracker.data.mapper.RuleRuleDtoMapper
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Link
import ru.akurbanoff.apptracker.domain.model.LinkWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.storage.AppTrackerDatabase
import ru.akurbanoff.apptracker.storage.dao.LinkDao
import ru.akurbanoff.apptracker.storage.dao.RulesDao
import javax.inject.Inject

class LinkRepository @Inject constructor(
    private val database: AppTrackerDatabase,
    private val linkDao: LinkDao,
    private val rulesDao: RulesDao,
    private val ruleDtoRuleMapper: RuleDtoRuleMapper,
    private val ruleRuleDtoMapper: RuleRuleDtoMapper,
    private val linkToDtoMapper: LinkToDtoMapper,
    private val linkDtoToLinkMapper: LinkDtoToLinkMapper
) {
    val cachedLinks: MutableList<LinkWithRules> = mutableListOf()

    suspend fun getAllLinks(): Flow<List<LinkWithRules>>{
        return linkDao.getAll().map { linkDtos ->
            linkDtos.map { linkDto ->
                val link = linkDtoToLinkMapper.invoke(linkDto)
                val rules = rulesDao.getRulesByLink(link.link).map {
                    ruleDtoRuleMapper.invoke(it)
                }
                LinkWithRules(link, rules)
            }
        }.map {
            cachedLinks.clear()
            cachedLinks.addAll(it)
            it
        }
    }

    suspend fun createLink(link: Link){
        linkDao.insertOrUpdate(linkToDtoMapper.invoke(link))
    }

    suspend fun checkLink(link: String, enabled: Boolean) {
        linkDao.checkApp(link, enabled)
    }

    suspend fun getRulesFor(link: String): List<Rule> {
        return rulesDao.getRulesByLink(link).map { ruleDtoRuleMapper.invoke(it) }
    }

    suspend fun addRule(rule: Rule) {
        rulesDao.insertOrUpdateRule(ruleRuleDtoMapper.invoke(rule))
    }
}