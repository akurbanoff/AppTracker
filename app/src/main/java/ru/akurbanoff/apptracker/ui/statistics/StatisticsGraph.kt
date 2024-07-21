package ru.akurbanoff.apptracker.ui.statistics

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun StatisticsGraph(rootNavController: NavHostController) {
    val statisticsNavigator = rememberNavController()

    NavHost(
        navController = statisticsNavigator,
        startDestination = StatisticsFragment::class.java.name
    ){
        composable(StatisticsFragment::class.java.name){
            StatisticsFragment(statisticsNavigator).Main()
        }
    }
}