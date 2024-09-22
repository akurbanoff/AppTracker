package ru.akurbanoff.apptracker.ui.app_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.emergency_access.EmergencyAccessFragment
import ru.akurbanoff.apptracker.ui.navigation.NavGraphs

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

        composable<App> { navBackStackEntry ->
            val app = navBackStackEntry.toRoute<App>()
            EmergencyAccessFragment(appListNavigator, app).Main()
        }

//        composable(
//            route = NavGraphs.EmergencyAccessGraph.route + "/{app}",
//            arguments = listOf(navArgument("app"){type = NavType.ParcelableType(App::class.java)})
//        ){ backStackEntry ->
//            val app = backStackEntry.arguments?.get("app") as App
//            EmergencyAccessFragment(appListNavigator, app).Main()
//        }
    }
}