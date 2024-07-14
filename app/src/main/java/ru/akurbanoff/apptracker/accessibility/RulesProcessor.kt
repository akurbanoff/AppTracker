package ru.akurbanoff.apptracker.accessibility

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.Rule
import java.time.LocalTime
import javax.inject.Inject


class RulesProcessor @Inject constructor(
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

    fun registerInAppTime(s: String, secondsInApp: Int) = coroutineScope.launch {
        appsRepository.registerInAppTime(s, secondsInApp)
    }

}
