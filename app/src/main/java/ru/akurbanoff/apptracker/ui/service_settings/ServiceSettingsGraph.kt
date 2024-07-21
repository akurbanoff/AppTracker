package ru.akurbanoff.apptracker.ui.service_settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ServiceSettingsGraph(modifier: Modifier = Modifier, rootNavController: NavHostController) {
    val serviceSettingsNavigator = rememberNavController()

    NavHost(
        navController = serviceSettingsNavigator,
        startDestination = ServiceSettingsFragment::class.java.name,
    ){
        composable(ServiceSettingsFragment::class.java.name){
            ServiceSettingsFragment(serviceSettingsNavigator).Main()
        }
    }
}