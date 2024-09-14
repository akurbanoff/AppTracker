package ru.akurbanoff.apptracker.ui.emergency_access

import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.data.repository.AppsRepository
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import javax.inject.Inject

@HiltViewModel
class EmergencyAccessViewModel @Inject constructor(
    private val appsRepository: AppsRepository
): ViewModel() {
    val _state = MutableStateFlow(AppListDetailState())
    val state: StateFlow<AppListDetailState>
        get() = _state

    private var imageJob: Job? = null

    private val _bitmapMap = MutableStateFlow<HashMap<String, Bitmap>>(hashMapOf())
    val bitmapMap: StateFlow<HashMap<String, Bitmap>> = _bitmapMap

    fun requestImageFor(app: AppWithRules) {
        viewModelScope.launch(Dispatchers.IO) {
            val packageManager = AppTrackerApplication.INSTANCE?.packageManager
            val appIcon = packageManager?.getApplicationIcon(app.app.packageName)?.toBitmap() ?: return@launch
            _bitmapMap.value[app.app.packageName] = appIcon

            _bitmapMap.value = _bitmapMap.value
        }
    }

    data class AppListDetailState(
        val timeLimitRule: Rule.TimeLimitRule? = null,
        val hourOfTheDayRangeRule: Rule.HourOfTheDayRangeRule? = null
    )
}