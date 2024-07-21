package ru.akurbanoff.apptracker.ui.app_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.utils.UiState
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
     private val appsRepository: AppsRepository
): ViewModel(){

    private val _state = MutableStateFlow(AppListState())
    val state: StateFlow<AppListState> = _state

    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(apps = UiState.Loading)
            }

            val apps = appsRepository.getAllApps().sortedBy { it.app.name }

            _state.update {
                it.copy(
                    apps = UiState.Success(apps),
                    amountOfEnabledApps = apps.count { app -> app.app.enabled }
                )
            }
        }
    }

    fun switchAllApps() {
        _state.value = _state.value.copy(isAllAppsEnabled = !_state.value.isAllAppsEnabled)
    }

    fun checkApp(app: AppWithRules, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(amountOfEnabledApps =
                    if (enabled) it.amountOfEnabledApps + 1 else it.amountOfEnabledApps - 1
                )
            }
            appsRepository.updateApp(app.app.copy(enabled = enabled))
        }
    }

    fun onSearch(query: String) {

    }

    data class AppListState(
        val apps: UiState = UiState.Loading,
        val isAllAppsEnabled: Boolean = false,
        var amountOfEnabledApps: Int = 0
    )
}
