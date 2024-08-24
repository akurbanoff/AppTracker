package ru.akurbanoff.apptracker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.Rule
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val appsRepository: AppsRepository,
) : ViewModel() {

    private var actualizeApps: Job? = null

    fun init() {
        if (actualizeApps != null) return
        actualizeApps = viewModelScope.launch(Dispatchers.IO) {
            appsRepository.actualizeAppList()
            appsRepository.getAllApps("", true).collect()
        }
    }

}


