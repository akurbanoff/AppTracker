package ru.akurbanoff.apptracker.ui.service_settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ServiceSettingsViewModel: ViewModel(){
    private val _state = MutableStateFlow(ServiceSettingsState())
    val state: StateFlow<ServiceSettingsState> = _state

    data class ServiceSettingsState(
        val serviceTimeStart: String = "",
        val serviceTimeEnd: String = ""
    )
}