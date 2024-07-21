package ru.akurbanoff.apptracker.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.akurbanoff.apptracker.data.mapper.AppAppDtoMapper
import ru.akurbanoff.apptracker.data.mapper.AppDtoAppMapper
import ru.akurbanoff.apptracker.data.mapper.AppStateAppStateDtoMapper
import ru.akurbanoff.apptracker.data.mapper.AppStateDtoAppStateMapper
import ru.akurbanoff.apptracker.data.mapper.RuleDtoRuleMapper
import ru.akurbanoff.apptracker.data.mapper.RuleRuleDtoMapper
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.AppState
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.storage.AppTrackerDatabase
import ru.akurbanoff.apptracker.storage.dao.AppStatesDao
import ru.akurbanoff.apptracker.storage.dao.AppsDao
import ru.akurbanoff.apptracker.storage.dao.RulesDao
import javax.inject.Inject


class AppsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppTrackerDatabase,
    private val appsDao: AppsDao,
    private val rulesDao: RulesDao,
    private val appStatesDao: AppStatesDao,
    private val appDtoAppMapper: AppDtoAppMapper,
    private val appAppDtoMapper: AppAppDtoMapper,
    private val ruleDtoRuleMapper: RuleDtoRuleMapper,
    private val ruleRuleDtoMapper: RuleRuleDtoMapper,
    private val appStateAppStateDtoMapper: AppStateAppStateDtoMapper,
    private val appStateDtoAppStateMapper: AppStateDtoAppStateMapper,
) {

    private val packageManager = context.packageManager

    suspend fun actualizeAppList() {
        val installedApps = getInstalledApps()
        val flattenToList = appsDao.getList()
        val savedAppConfigs = flattenToList.map { appDtoAppMapper.invoke(it, packageManager) }

        for (installedApp in installedApps) {
            val packageName = installedApp.activityInfo?.packageName ?: continue
            val newApp = savedAppConfigs.firstOrNull { it.packageName == packageName } == null
            if (newApp) {
                val app = App(enabled = false, packageName = packageName)
                updateApp(app)
            }
        }

        for (appConfig in savedAppConfigs) {
            val appIsDeleted =
                installedApps.firstOrNull { it.activityInfo?.packageName == appConfig.packageName } == null
            if (appIsDeleted) deleteApp(appConfig)
        }
    }

    suspend fun getAllApps(query: String = "", allApps: Boolean): Flow<List<AppWithRules>> {
        val appDtoFlow = appsDao.getAll()
        return appDtoFlow.map { appDtos ->
            appDtos.map { appDto ->
                val app = appDtoAppMapper.invoke(appDto, packageManager)
                val rules = rulesDao.getRulesByPackage(app.packageName).map { ruleDtoRuleMapper.invoke(it) }
                AppWithRules(app, rules)
            }.filter {
                val containsQuery = it.app.name?.contains(query, true) == true
                val include = if (allApps) true else it.app.enabled
                include && containsQuery
            }
        }
    }

    suspend fun updateApp(app: App) {
        appsDao.insertOrUpdate(appAppDtoMapper.invoke(app))
    }

    suspend fun deleteApp(app: App) {
        appsDao.delete(app.packageName)
    }

    suspend fun addRule(rule: Rule) {
        rulesDao.insertOrUpdateRule(ruleRuleDtoMapper.invoke(rule))
    }

    suspend fun deleteRule(rule: Rule) {
        rulesDao.deleteRule(ruleRuleDtoMapper.invoke(rule))
    }

    suspend fun updateAppState(appState: AppState) {
        appStatesDao.updateStates(appStateAppStateDtoMapper.invoke(appState))
    }

    suspend fun getAppStateFor(currentAppPackageName: String): AppState? =
        getAppStates().firstOrNull { it.packageName == currentAppPackageName }

    suspend fun registerInAppTime(packageName: String, timeInApp: Int) = database.withTransaction {
        val appState = getAppStateFor(packageName) ?: AppState(packageName, timeInApp)
        updateAppState(appState.copy(timeInApp = appState.timeInApp + timeInApp))
    }

    private suspend fun getAppStates() = appStatesDao.getAppState()
        .map { appStateDtoAppStateMapper.invoke(it) }

    private fun getInstalledApps(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(mainIntent, 0)
    }

    suspend fun getList(): List<AppWithRules> {
        return appsDao.getList().map { appDto ->
            val app = appDtoAppMapper.invoke(appDto, packageManager)
            val rules = rulesDao.getRulesByPackage(app.packageName).map { ruleDtoRuleMapper.invoke(it) }
            AppWithRules(app, rules)
        }
    }
}
