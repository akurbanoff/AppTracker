package ru.akurbanoff.apptracker.ui.app_list

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.akurbanoff.apptracker.AppTrackerApplication
import ru.akurbanoff.apptracker.di.getApplicationComponent
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import javax.inject.Inject

class AppListFragment(
    private val navController: NavHostController
) {
    lateinit var appListViewModel: AppListViewModel

    @Composable
    fun main() {

        appListViewModel = viewModel()

        appListViewModel.getApps()

        val state by appListViewModel.state.collectAsState()


    }
}