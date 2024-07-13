package ru.akurbanoff.apptracker.ui.navigation

sealed class NavDestinations(val route: String) {
    data object About : NavDestinations("about")
    data object ServiceSettings : NavDestinations("service_settings")
    data object AppList : NavDestinations("app_list")
    data object Statistics : NavDestinations("statistics")
}