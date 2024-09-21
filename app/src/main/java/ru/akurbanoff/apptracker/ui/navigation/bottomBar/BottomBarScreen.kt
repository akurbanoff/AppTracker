package ru.akurbanoff.apptracker.ui.navigation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import ru.akurbanoff.apptracker.ui.navigation.NavGraphs

sealed class BottomBarScreen(
    val route: String,
    val iconOn: ImageVector,
    val iconOff: ImageVector,
    val screenType: NavGraphs
) {
    data object AboutScreen : BottomBarScreen(
        NavGraphs.AboutGraph.route,
        Icons.Default.Info,
        Icons.Outlined.Info,
        NavGraphs.AboutGraph
    )
    data object AppListScreen : BottomBarScreen(
        NavGraphs.AppListGraph.route,
        Icons.Default.Apps,
        Icons.Default.Apps,
        NavGraphs.AppListGraph
    )
    data object ServiceSettingsScreen : BottomBarScreen(
        NavGraphs.ServiceSettingsGraph.route,
        Icons.Default.Tune,
        Icons.Default.Tune,
        NavGraphs.ServiceSettingsGraph
    )
    data object StatisticsScreen : BottomBarScreen(
        NavGraphs.StatisticsGraph.route,
        Icons.Default.InsertChart,
        Icons.Default.InsertChartOutlined,
        NavGraphs.StatisticsGraph
    )
}