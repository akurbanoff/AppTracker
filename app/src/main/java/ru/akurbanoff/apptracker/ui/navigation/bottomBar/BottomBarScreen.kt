package ru.akurbanoff.apptracker.ui.navigation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import ru.akurbanoff.apptracker.ui.navigation.NavDestinations

sealed class BottomBarScreen(
    val route: String,
    val iconOn: ImageVector,
    val iconOff: ImageVector,
    val screenType: NavDestinations
) {
    data object AboutScreen : BottomBarScreen(
        "about",
        Icons.Default.Info,
        Icons.Default.Info,
        NavDestinations.About
    )
    data object AppListScreen : BottomBarScreen(
        "app_list",
        Icons.Default.Apps,
        Icons.Default.Apps,
        NavDestinations.AppList
    )
    data object ServiceSettingsScreen : BottomBarScreen(
        "service_settings",
        Icons.Default.Tune,
        Icons.Default.Tune,
        NavDestinations.ServiceSettings
    )
    data object StatisticsScreen : BottomBarScreen(
        "statistics",
        Icons.Default.InsertChart,
        Icons.Default.InsertChartOutlined,
        NavDestinations.Statistics
    )
}