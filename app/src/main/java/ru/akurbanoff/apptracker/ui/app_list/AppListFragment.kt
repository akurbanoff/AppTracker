package ru.akurbanoff.apptracker.ui.app_list

import android.graphics.drawable.Drawable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.compose_recyclerview.ComposeRecyclerView
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.utils.LifeScreen
import ru.akurbanoff.apptracker.ui.utils.UiState

class AppListFragment(
    private val navController: NavHostController,
) {
    private lateinit var appListViewModel: AppListViewModel

    @Composable
    fun Main() {
        appListViewModel = hiltViewModel<AppListViewModel>()
        val state by appListViewModel.state.collectAsState()
        //appListViewModel.getApps()

        BackHandler {
            navController.popBackStack()
        }

        ScreenContent(
            apps = state.apps,
            isAllAppsEnabled = state.isAllAppsEnabled ?: false
        )
    }

    @Composable
    private fun ScreenContent(
        modifier: Modifier = Modifier,
        apps: UiState,
        isAllAppsEnabled: Boolean,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopScreenPart(isAllAppsEnabled = isAllAppsEnabled)
            AppList(apps = apps)
        }
    }

    @Composable
    private fun TopScreenPart(modifier: Modifier = Modifier, isAllAppsEnabled: Boolean) {
        val context = LocalContext.current
        Column(
            modifier = modifier
                .fillMaxWidth()
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Apps"
                )
                Icon(
                    imageVector= Icons.Default.Search,
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
        apps: UiState
    ) {
        when (apps) {
            is UiState.Error -> TODO()
            UiState.Loading -> {
                Loading()
            }
            is UiState.Success<*> -> {
                ComposeRecyclerView(
                    modifier = modifier.fillMaxWidth(),
                    items = apps.data as? List<AppWithRules> ?: emptyList(),
                    itemBuilder = { item, _ ->
                        AppItem(item = item)
                    }
                )
            }
        }
    }

    @Composable
    private fun AppItem(
        modifier: Modifier = Modifier,
        item: AppWithRules
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
                Image(
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
                    //appListViewModel.checkApp(item)
                }
            )
        }
    }

    @Composable
    private fun Loading(modifier: Modifier = Modifier) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
