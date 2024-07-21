package ru.akurbanoff.apptracker.ui.navigation

sealed class NavGraphs(val route: String) {
    data object AboutGraph: NavGraphs("about_graph")
    data object ServiceSettingsGraph : NavGraphs("service_settings_graph")
    data object AppListGraph : NavGraphs("app_list_graph")
    data object StatisticsGraph : NavGraphs("statistics_graph")
}