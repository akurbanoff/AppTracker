package ru.akurbanoff.apptracker.ui.service_settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ServiceSettingsViewModel @Inject constructor(): ViewModel(){
    private val _state = MutableStateFlow(ServiceSettingsState())
    val state: StateFlow<ServiceSettingsState> = _state

    data class ServiceSettingsState(
        val serviceTimeStart: String = "",
        val serviceTimeEnd: String = ""
    )
}