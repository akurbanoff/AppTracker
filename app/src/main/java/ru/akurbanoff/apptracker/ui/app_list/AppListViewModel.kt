package ru.akurbanoff.apptracker.ui.app_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.utils.UiState
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AppListState())
    val state: StateFlow<AppListState> = _state

    private val shouldShowAllApps = MutableStateFlow(false)
    private val searchQueryState = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(apps = UiState.Loading) }

            searchQueryState.flatMapLatest { query ->
                shouldShowAllApps.flatMapLatest { show ->
                    appsRepository.getAllApps(allApps = show, query = query)
                }
            }.collectLatest {
                updateState(it)
            }
        }
    }

    fun switchAllApps() {
        val isAllAppsEnabled = !_state.value.isAllAppsEnabled
        shouldShowAllApps.value = isAllAppsEnabled
        _state.value = _state.value.copy(isAllAppsEnabled = isAllAppsEnabled)
    }

    fun checkApp(appWithRules: AppWithRules, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.updateApp(appWithRules.app.copy(enabled = enabled))
        }
    }

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.actualizeAppList()
        }
    }

    private fun updateState(apps: List<AppWithRules>) = _state.update {
        it.copy(
            apps = UiState.Success(apps.sortedBy { appWithRules -> appWithRules.app.name }),
            amountOfEnabledApps = apps.count { app -> app.app.enabled }
        )
    }

    fun onSearch(query: String) {
        searchQueryState.value = query
    }

    data class AppListState(
        val apps: UiState = UiState.Loading,
        val isAllAppsEnabled: Boolean = true,
        var amountOfEnabledApps: Int = 0,
    )
}
