package ru.akurbanoff.apptracker.ui.emergency_access

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
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import java.util.Random
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getApps() {
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.getAllApps(allApps = true, query = "")
                .map {
                    Result.success(it)
                }.catch {
                    Result.failure<Throwable>(it)
                }.collectLatest { appsResult ->
                    when {
                        appsResult.isFailure -> {}
                        appsResult.isSuccess -> {
                            updateState(appsResult.getOrNull() ?: emptyList())
                        }
                    }
                }
        }
    }

    private fun updateState(apps: List<AppWithRules>) {
        val app = apps.sortedBy { apps -> apps.app.name }.first()
        _state.update {
            it.copy(
                app = app,
            )
        }
        requestImageFor(app)
    }

    fun requestImageFor(app: AppWithRules) {
        viewModelScope.launch(Dispatchers.IO) {
            val packageManager = AppTrackerApplication.INSTANCE?.packageManager
            val appIcon = packageManager?.getApplicationIcon(app.app.packageName)?.toBitmap() ?: return@launch
            _bitmapMap.value[app.app.packageName] = appIcon

            _bitmapMap.value = _bitmapMap.value
        }
    }

    fun setTimeLimitRule(packageName: String, enabled: Boolean, hour: Int, minute: Int){
        val limitInSeconds = ((hour * 60) + minute) * 60
        val rule = Rule.TimeLimitRule(id = Random().nextInt(), packageName = packageName, enabled = enabled, limitInSeconds = limitInSeconds)
        viewModelScope.launch(Dispatchers.IO) {
            appsRepository.addRule(rule)
        }
    }

    data class AppListDetailState(
        val app: AppWithRules? = null,
        val timeLimitRule: Rule.TimeLimitRule? = null,
        val hourOfTheDayRangeRule: Rule.HourOfTheDayRangeRule? = null
    )
}