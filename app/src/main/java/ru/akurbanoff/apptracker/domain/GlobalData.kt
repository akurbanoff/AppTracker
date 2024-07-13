package ru.akurbanoff.apptracker.domain

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.akurbanoff.apptracker.ui.navigation.NavDestinations

var GlobalData = SingleLiveEvent<DataSingleLive>()

data class DataSingleLive(
    val currentBottomNavigationMenu: NavDestinations = NavDestinations.ServiceSettings,
)

fun gDChangeBottomNavigationMenu(newNavMenu: NavDestinations) =
    funInScope { it.copy(currentBottomNavigationMenu = newNavMenu) }

@OptIn(DelicateCoroutinesApi::class)
private fun funInScope(unit: (DataSingleLive) -> DataSingleLive) {
    GlobalScope.launch launchMain@{
        withContext(Dispatchers.Main) {
            GlobalData.value = unit.invoke(GlobalData.value ?: DataSingleLive())
            this@launchMain.cancel()
        }
    }
}
