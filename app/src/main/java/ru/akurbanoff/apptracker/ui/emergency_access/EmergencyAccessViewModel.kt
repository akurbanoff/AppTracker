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
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class EmergencyAccessViewModel @Inject constructor(): ViewModel() {
    private val _bitmapMap = MutableStateFlow<HashMap<String, Bitmap>>(hashMapOf())
    val bitmapMap: StateFlow<HashMap<String, Bitmap>> = _bitmapMap

    fun requestImageFor(app: App) {
        viewModelScope.launch(Dispatchers.IO) {
            val packageManager = AppTrackerApplication.INSTANCE?.packageManager
            val appIcon = packageManager?.getApplicationIcon(app.packageName)?.toBitmap() ?: return@launch
            _bitmapMap.value[app.packageName] = appIcon

            _bitmapMap.value = _bitmapMap.value
        }
    }
}