package ru.akurbanoff.apptracker.ui.app_list

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import java.util.Random
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AppListState())
    val state: StateFlow<AppListState> = _state

    private val shouldShowAllApps = MutableStateFlow(true)
    private val searchQueryState = MutableStateFlow("")

    private var apps: List<AppWithRules> = listOf()

    private var imageJob: Job? = null

    private val _bitmapMap = MutableStateFlow<HashMap<String, Bitmap>>(hashMapOf())
    val bitmapMap: StateFlow<HashMap<String, Bitmap>> = _bitmapMap

    init {
        updateState(apps = appsRepository.cachedApps)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQueryState.flatMapLatest { query ->
                shouldShowAllApps.flatMapLatest { show ->
                    appsRepository.getAllApps(allApps = show, query = query)
                        .map {
                            Result.success(it)
                        }.catch {
                            Result.failure<Throwable>(it)
                        }
                }
            }.collectLatest { appsResult ->
                when {
                    appsResult.isFailure -> {
                        _state.update {
                            it.copy(
                                isAppsFailure = appsResult.exceptionOrNull()
                            )
                        }
                    }
                    appsResult.isSuccess -> {
                        _state.update {
                            it.copy(
                                isAppsFailure = null,
                            )
                        }
                        updateState(appsResult.getOrNull() ?: emptyList())
                    }
                }
            }
        }
    }

    fun switchAllApps() {
        val isAllAppsEnabled = !_state.value.isAllAppsEnabled
        shouldShowAllApps.value = isAllAppsEnabled
        _state.update { it.copy(isAllAppsEnabled = isAllAppsEnabled) }
    }

    fun checkApp(itemApp: AppWithRules, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.checkApp(itemApp.app.packageName, enabled)
        }
    }

    private fun requestImagesFor(itemApps: List<AppWithRules>) {
        if (imageJob != null) return
        if (itemApps.size == bitmapMap.value.size) return

        imageJob = viewModelScope.launch(Dispatchers.IO) {
            for (itemApp in itemApps) {
                val packageManager = AppTrackerApplication.INSTANCE?.packageManager
                val appIcon = packageManager?.getApplicationIcon(itemApp.app.packageName)?.toBitmap() ?: return@launch
                _bitmapMap.value[itemApp.app.packageName] = appIcon
            }

            _bitmapMap.value = _bitmapMap.value
        }

        updateState(apps)
    }

    private fun updateState(apps: List<AppWithRules>) = _state.update {
        this.apps = apps
        requestImagesFor(apps)

        it.copy(
            apps = apps.sortedBy { apps -> apps.app.name },
            amountOfEnabledApps = apps.count { app -> app.app.enabled }
        )
    }

    fun onSearch(query: String) {
        searchQueryState.value = query
    }

    fun getIconFor(packageManager: PackageManager, app: App) =
        packageManager.getApplicationIcon(app.packageName).toBitmap()

    fun setTimeLimitRule(packageName: String, enabled: Boolean, hour: Int, minute: Int){
        val limitInSeconds = ((hour * 60) + minute) * 60
        val rule = Rule.TimeLimitRule(id = Random(System.currentTimeMillis()).nextInt(), packageName = packageName, enabled = enabled, limitInSeconds = limitInSeconds)
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.addRule(rule)
        }
    }

    fun setHourOfTheDayRangeRule(packageName: String, enabled: Boolean, hour: Int, minute: Int){

    }

    data class AppListState(
        val apps: List<AppWithRules> = emptyList(),
        val isAllAppsEnabled: Boolean = true,
        var amountOfEnabledApps: Int = 0,
        var isAppsFailure: Throwable? = null,
    )
}
