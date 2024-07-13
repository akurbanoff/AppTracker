package ru.akurbanoff.apptracker.ui.app_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppListViewModel: ViewModel(){

    private val _state = MutableStateFlow(AppListState())
    val state: StateFlow<AppListState> = _state

    data class AppListState(
        val apps: List<String> = emptyList()
    )
}