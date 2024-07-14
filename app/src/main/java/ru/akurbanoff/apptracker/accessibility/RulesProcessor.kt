package ru.akurbanoff.apptracker.accessibility

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.Rule
import java.time.LocalTime


class RulesProcessor(
    private val appsRepository: AppsRepository,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun processRules(currentAppPackageName: String, onTriggered: () -> Unit) = coroutineScope.launch {
        val appState = appsRepository.getAppStateFor(currentAppPackageName) ?: return@launch
        val apps = appsRepository.getAllApps()

        apps.firstOrNull { it.app.packageName == currentAppPackageName }?.let { app ->
            for (rule in app.rules) {
                val invoke = when (rule) {
                    is Rule.TimeLimitRule -> rule.condition.invoke(arrayOf(appState.timeInApp))
                    is Rule.HourOfTheDayRangeRule -> rule.condition.invoke(arrayOf(LocalTime.now().hour))
                }

                if (!invoke) onTriggered.invoke()
            }
        }
    }

    fun registerInAppTime(packageName: String, timeInApp: Int) = coroutineScope.launch {
        val appState = appsRepository.getAppStateFor(packageName) ?: return@launch
        appsRepository.updateAppState(appState.copy(timeInApp = appState.timeInApp + timeInApp))
    }

}
