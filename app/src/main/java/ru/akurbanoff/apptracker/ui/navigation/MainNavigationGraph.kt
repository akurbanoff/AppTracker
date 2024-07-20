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
import ru.akurbanoff.apptracker.ui.app_list.AppListFragment
import ru.akurbanoff.apptracker.ui.navigation.bottomBar.BottomBar
import ru.akurbanoff.apptracker.ui.service_settings.ServiceSettingsFragment
import ru.akurbanoff.apptracker.ui.statistics.StatisticsFragment

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
            modifier = Modifier.padding(padding),
            startDestination = NavDestinations.ServiceSettings.route,
            navController = navigator
        ) {
            composable(NavDestinations.ServiceSettings.route) {
                ServiceSettingsFragment(navigator).main()
            }

            composable(NavDestinations.About.route) {
                AboutFragment(navigator).main()
            }

            composable(NavDestinations.AppList.route) {
                AppListFragment(navigator).Main()
            }

            composable(NavDestinations.Statistics.route) {
                StatisticsFragment(navigator).main()
            }
        }
    }
}