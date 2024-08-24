package ru.akurbanoff.apptracker.ui.app_list

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.utils.UiState
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
    val imageBitmaps = hashMapOf<AppWithRules, Bitmap>()

    init {
        updateState(apps = appsRepository.cachedApps)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQueryState.flatMapLatest { query ->
                shouldShowAllApps.flatMapLatest { show ->
                    appsRepository.getAllApps(allApps = show, query = query)
                }
            }.collectLatest { app ->
                updateState(app)
            }
        }
    }

    fun switchAllApps() {
        val isAllAppsEnabled = !_state.value.isAllAppsEnabled
        shouldShowAllApps.value = isAllAppsEnabled
        _state.value = _state.value.copy(isAllAppsEnabled = isAllAppsEnabled)
    }

    fun checkApp(itemApp: AppWithRules, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.checkApp(itemApp.app.packageName, enabled)
        }
    }

    private fun requestImagesFor(itemApps: List<AppWithRules>) {
        if (imageJob != null) return
        if (itemApps.size == imageBitmaps.size) return

        imageJob = viewModelScope.launch(Dispatchers.IO) {
            for (itemApp in itemApps) {
                val packageManager = AppTrackerApplication.INSTANCE?.packageManager
                val appIcon = packageManager?.getApplicationIcon(itemApp.app.packageName)?.toBitmap() ?: return@launch
                imageBitmaps[itemApp] = appIcon
            }
        }

        updateState(apps)
    }

    private fun updateState(apps: List<AppWithRules>) = _state.update {
        this.apps = apps
        requestImagesFor(apps)

        it.copy(
            apps = UiState.Success(apps.sortedBy { apps -> apps.app.name }),
            amountOfEnabledApps = apps.count { app -> app.app.enabled }
        )
    }

    fun onSearch(query: String) {
        searchQueryState.value = query
    }

    fun getIconFor(packageManager: PackageManager, app: App) =
        packageManager.getApplicationIcon(app.packageName).toBitmap()

    data class AppListState(
        val apps: UiState = UiState.Success(listOf<AppWithRules>()),
        val isAllAppsEnabled: Boolean = true,
        var amountOfEnabledApps: Int = 0,
    )
}
