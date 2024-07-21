package ru.akurbanoff.apptracker.ui.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AboutGraph(modifier: Modifier = Modifier, rootNavController: NavHostController) {
    val aboutGraphNavigator = rememberNavController()

    NavHost(
        navController = aboutGraphNavigator,
        startDestination = AboutFragment::class.java.name,
    ){
        composable(AboutFragment::class.java.name) {
            AboutFragment(aboutGraphNavigator).Main()
        }
    }
}