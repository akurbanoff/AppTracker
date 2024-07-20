package ru.akurbanoff.apptracker.ui.service_settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.di.getApplicationComponent
import ru.akurbanoff.apptracker.ui.app_list.AppListViewModel
import javax.inject.Inject

class ServiceSettingsFragment(
    private val navController: NavHostController
) {

    lateinit var serviceSettingsViewModel: ServiceSettingsViewModel

    @Composable
    fun main() {
        serviceSettingsViewModel = getApplicationComponent(LocalContext.current).serviceSettingsViewModel
        val state = serviceSettingsViewModel.state.collectAsState()
    }
}