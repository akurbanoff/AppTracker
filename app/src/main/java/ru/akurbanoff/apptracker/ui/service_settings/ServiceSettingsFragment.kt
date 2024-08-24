package ru.akurbanoff.apptracker.ui.service_settings

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import ru.akurbanoff.apptracker.di.getApplicationComponent

class ServiceSettingsFragment(
    private val navController: NavHostController,
) {

    lateinit var serviceSettingsViewModel: ServiceSettingsViewModel

    @Composable
    fun Main() {
        serviceSettingsViewModel = getApplicationComponent(LocalContext.current).serviceSettingsViewModel
        val state = serviceSettingsViewModel.state.collectAsState()
    }
}
