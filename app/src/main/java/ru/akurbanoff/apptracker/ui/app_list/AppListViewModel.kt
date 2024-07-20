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
import javax.inject.Inject

class AppListViewModel @Inject constructor(
     private val appsRepository: AppsRepository
): ViewModel(){

    private val _state = MutableStateFlow(AppListState())
    val state: StateFlow<AppListState> = _state

    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val apps = appsRepository.getAllApps()
            //_state.value = _state.value.copy(apps = apps)
            _state.update {
                it.copy(apps = apps)
            }
        }
    }

    fun switchAllApps() {
        _state.value = _state.value.copy(isAllAppsEnabled = !_state.value.isAllAppsEnabled)
    }

    fun checkApp(app: AppWithRules) {
        _state.value = _state.value.copy(apps = _state.value.apps.map {
            if (it.app.packageName == app.app.packageName) {
                it.copy(app = it.app.copy(enabled = !it.app.enabled))
            } else {
                it
            }
        })
    }

    data class AppListState(
        val apps: List<AppWithRules> = emptyList(),
        val isAllAppsEnabled: Boolean = false
    )
}