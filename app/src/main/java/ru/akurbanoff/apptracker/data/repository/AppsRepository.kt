package ru.akurbanoff.apptracker.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.core.graphics.drawable.toBitmap
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
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
    suspend fun getAllApps(): List<AppWithRules> {
        val installedApps = getInstalledApps()
        val result = mutableListOf<AppWithRules>()
        val packageManager = context.packageManager
        val savedAppConfigs = appsDao.getApps().map { appDtoAppMapper.invoke(it) }

        for (app in installedApps) {
            val packageName = app.activityInfo.packageName
            val appName = app.loadLabel(packageManager)?.toString() ?: packageName
            val savedApp = savedAppConfigs.firstOrNull { it.packageName == packageName }
            val appIcon = packageManager.getApplicationIcon(packageName).toBitmap()

            if (savedApp != null) {
                val updatedApp = savedApp.copy(name = appName, icon = appIcon)
                val rules = rulesDao.getRulesByAppId(updatedApp.id).map { ruleDtoRuleMapper.invoke(it) }
                result.add(AppWithRules(updatedApp, rules))
            } else {
                val newApp = App(-1, appName, appIcon, packageName, false)
                val newAppWithRules = AppWithRules(app = newApp, rules = listOf())
                result.add(newAppWithRules)
            }
        }

        return result.toList()
    }

    suspend fun updateApp(app: App) {
        appsDao.insertOrUpdateApp(appAppDtoMapper.invoke(app))
    }

    suspend fun addRule(rule: Rule) {
        rulesDao.insertOrUpdateRule(ruleRuleDtoMapper.invoke(rule))
    }

    suspend fun deleteRule(rule: Rule) {
        rulesDao.deleteRule(ruleRuleDtoMapper.invoke(rule))
    }

    suspend fun deleteRulesByAppId(id: Int) {
        appsDao.deleteRulesByAppId(id)
    }

    suspend fun updateAppState(appState: AppState) {
        appStatesDao.updateStates(appStateAppStateDtoMapper.invoke(appState))
    }

    suspend fun getAppStateFor(currentAppPackageName: String): AppState? =
        getAppStates().firstOrNull { it.packageName == currentAppPackageName }

    suspend fun getAppStates() = appStatesDao.getAppState()
        .map { appStateDtoAppStateMapper.invoke(it) }

    suspend fun registerInAppTime(packageName: String, timeInApp: Int) = database.withTransaction {
        val appState = getAppStateFor(packageName) ?: AppState(packageName, timeInApp)
        updateAppState(appState.copy(timeInApp = appState.timeInApp + timeInApp))
    }

    private fun getInstalledApps(): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return context.packageManager.queryIntentActivities(mainIntent, 0)
    }
}
