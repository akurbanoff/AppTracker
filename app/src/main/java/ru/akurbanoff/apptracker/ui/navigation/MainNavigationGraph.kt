package ru.akurbanoff.apptracker.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.akurbanoff.apptracker.ui.about.AboutFragment
import ru.akurbanoff.apptracker.ui.about.AboutGraph
import ru.akurbanoff.apptracker.ui.app_list.AppListFragment
import ru.akurbanoff.apptracker.ui.app_list.AppListGraph
import ru.akurbanoff.apptracker.ui.navigation.bottomBar.BottomBar
import ru.akurbanoff.apptracker.ui.service_settings.ServiceSettingsFragment
import ru.akurbanoff.apptracker.ui.service_settings.ServiceSettingsGraph
import ru.akurbanoff.apptracker.ui.statistics.StatisticsFragment
import ru.akurbanoff.apptracker.ui.statistics.StatisticsGraph

@Composable
fun MainNavigationGraph(modifier: Modifier = Modifier) {
    val navigator = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(navigator)
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
            startDestination = NavGraphs.ServiceSettingsGraph.route,
            navController = navigator
        ) {
            composable(NavGraphs.ServiceSettingsGraph.route) {
                ServiceSettingsGraph(rootNavController = navigator)
            }

            composable(NavGraphs.AboutGraph.route) {
                AboutGraph(rootNavController = navigator)
            }

            composable(NavGraphs.AppListGraph.route) {
                AppListGraph(rootNavController = navigator)
            }

            composable(NavGraphs.StatisticsGraph.route) {
                StatisticsGraph(rootNavController = navigator)
            }
        }
    }
}