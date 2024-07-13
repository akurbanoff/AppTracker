package ru.akurbanoff.apptracker.ui.navigation.bottomBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.akurbanoff.apptracker.domain.gDChangeBottomNavigationMenu

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.ServiceSettingsScreen,
        BottomBarScreen.AppListScreen,
        BottomBarScreen.StatisticsScreen,
        BottomBarScreen.AboutScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .height(60.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {

        screens.forEach { screen ->
            NavigationBarItem(
                selected = false,
                onClick = {
                    //gDChangeBottomNavigationMenu(screen.screenType)
                    navController.navigate(screen.route) {
                        navController.graph.findStartDestination().id.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (currentDestination?.route == screen.route) {
                        Icon(
                            imageVector = screen.iconOn,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = screen.iconOff,
                            contentDescription = null
                        )
                    }
                })
        }
    }
}