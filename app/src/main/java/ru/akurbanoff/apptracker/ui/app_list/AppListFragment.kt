package ru.akurbanoff.apptracker.ui.app_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.compose_recyclerview.ComposeRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        BackHandler {
            navController.popBackStack()
        }

        LifeScreen(
            onResume = {
                appListViewModel.getApps()
            },
            onCreate = {
                appListViewModel.init()
            }
        )

        ScreenContent(
            apps = state.apps,
            isAllAppsEnabled = state.isAllAppsEnabled,
            amountOfEnabledApps = state.amountOfEnabledApps
        )
    }

    @Composable
    private fun ScreenContent(
        modifier: Modifier = Modifier,
        apps: UiState,
        isAllAppsEnabled: Boolean,
        amountOfEnabledApps: Int,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopScreenPart(isAllAppsEnabled = isAllAppsEnabled, amountOfEnabledApps = amountOfEnabledApps)
            AppList(apps = apps, isAllAppsEnabled = isAllAppsEnabled)
        }
    }

    @Composable
    private fun TopScreenPart(
        modifier: Modifier = Modifier,
        isAllAppsEnabled: Boolean,
        amountOfEnabledApps: Int,
    ) {
        val context = LocalContext.current
        var isSearchEnabled by remember { mutableStateOf(false) }
        val searchJob = remember { mutableStateOf<Job?>(null) }
        val searchQuery = remember { mutableStateOf("") }
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Apps",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
                if (isSearchEnabled) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 7.dp)
                            .clip(MaterialTheme.shapes.medium),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clickable {
                                        isSearchEnabled = !isSearchEnabled
                                        searchQuery.value = ""
                                        appListViewModel.onSearch(query = searchQuery.value)
                                    }
                            )
                        },
                        value = searchQuery.value,
                        onValueChange = {
                            searchQuery.value = it
                            searchJob.value?.cancel()
                            searchJob.value = CoroutineScope(Dispatchers.Main).launch {
                                delay(1000) // задержка в миллисекундах для ожидания окончания ввода
                                appListViewModel.onSearch(query = searchQuery.value)
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,

                            )
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier
                                .size(34.dp)
                                .align(Alignment.Center)
                                .clickable {
                                    isSearchEnabled = !isSearchEnabled
                                }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.all_apps),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Switch(
                    checked = isAllAppsEnabled,
                    onCheckedChange = { appListViewModel.switchAllApps() }
                )
            }
            Text(
                text = context.getString(R.string.all_apps_info),
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.amount_of_apps),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Text(
                    text = amountOfEnabledApps.toString(),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }
    }

    @Composable
    private fun AppList(
        modifier: Modifier = Modifier,
        apps: UiState,
        isAllAppsEnabled: Boolean,
    ) {
        when (apps) {
            is UiState.Error -> TODO()
            UiState.Loading -> {
                Loading()
            }

            is UiState.Success<*> -> {
                LazyColumn {
                    val appWithRules = apps.data as List<AppWithRules>
                    for (i in appWithRules.indices) {
                        item { AppItem(item = appWithRules[i]) }
                    }
                }
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
                AsyncImage(
                    modifier = Modifier.size(34.dp),
                    model = ImageRequest.Builder(LocalContext.current).data(item.app.icon).build(),
                    contentDescription = null,
                    imageLoader = ImageLoader(LocalContext.current)
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text(
                    text = item.app.name ?: "",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
            Checkbox(
                checked = item.app.enabled,
                onCheckedChange = {
                    appListViewModel.checkApp(item, !item.app.enabled)
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
