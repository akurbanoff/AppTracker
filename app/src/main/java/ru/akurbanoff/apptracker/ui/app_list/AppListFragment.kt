package ru.akurbanoff.apptracker.ui.app_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.utils.LifeScreen

class AppListFragment(
    private val navController: NavHostController,
) {
    lateinit var appListViewModel: AppListViewModel

    @Composable
    fun main() {
        appListViewModel = hiltViewModel<AppListViewModel>()//getApplicationComponent(LocalContext.current).appListViewModel
        val state by appListViewModel.state.collectAsState()
        appListViewModel.getApps()

        BackHandler {
            navController.popBackStack()
        }

//        LifeScreen(
//            onResume = {
//                appListViewModel.getApps()
//            }
//        )

        ScreenContent(state = state)
    }

    @Composable
    private fun ScreenContent(
        modifier: Modifier = Modifier,
        state: AppListViewModel.AppListState,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopScreenPart(isAllAppsEnabled = state.isAllAppsEnabled)
            AppList(apps = state.apps)
        }
    }

    @Composable
    fun TopScreenPart(modifier: Modifier = Modifier, isAllAppsEnabled: Boolean) {
        val context = LocalContext.current
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Apps"
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.all_apps)
                )
                Switch(
                    checked = isAllAppsEnabled,
                    onCheckedChange = { appListViewModel.switchAllApps() }
                )
            }
            Text(
                text = context.getString(R.string.all_apps_info)
            )
        }
    }

    @Composable
    private fun AppList(
        modifier: Modifier = Modifier,
        apps: List<AppWithRules>,
    ) {
        LazyColumn {
            items(apps) { app ->
                AppItem(item = app)
            }

        }
    }

    @Composable
    private fun AppItem(
        modifier: Modifier = Modifier,
        item: AppWithRules,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    modifier = Modifier.size(34.dp),
                    bitmap = item.app.icon?.asImageBitmap()
                        ?: ImageBitmap.imageResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text(
                    text = item.app.name ?: "",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }

            Checkbox(
                checked = item.app.enabled,
                onCheckedChange = {
                    appListViewModel.checkApp(item)
                }
            )
        }
    }
}
