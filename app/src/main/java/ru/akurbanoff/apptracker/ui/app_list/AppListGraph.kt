package ru.akurbanoff.apptracker.ui.app_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppListGraph(modifier: Modifier = Modifier, rootNavController: NavHostController) {
    val appListNavigator = rememberNavController()

    NavHost(
        navController = appListNavigator,
        startDestination = AppListFragment::class.java.name
    ){
        composable(AppListFragment::class.java.name) {
            AppListFragment(appListNavigator).Main()
        }
    }
}