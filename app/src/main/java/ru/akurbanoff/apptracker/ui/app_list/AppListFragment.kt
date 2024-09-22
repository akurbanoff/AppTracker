package ru.akurbanoff.apptracker.ui.app_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.ui.navigation.NavGraphs
import ru.akurbanoff.apptracker.ui.utils.LifeScreen
import ru.akurbanoff.apptracker.ui.utils.formatSecondsToTime
import ru.akurbanoff.apptracker.ui.utils.formatTime

class AppListFragment(
    private val navController: NavHostController,
) {
    private lateinit var appListViewModel: AppListViewModel

    @Composable
    fun Main() {
        appListViewModel = hiltViewModel()
        val state by appListViewModel.state.collectAsState()
        val isSearchEnabled = remember { mutableStateOf(false) }
        val searchQuery = remember { mutableStateOf("") }

        LifeScreen(
            onResume = {
                appListViewModel.getApps()
            }
        )

        BackHandler {
            navController.popBackStack()
            searchQuery.value = ""
            appListViewModel.onSearch(query = searchQuery.value)
            isSearchEnabled.value = false
        }

        ScreenContent(
            apps = state.apps,
            isAllAppsEnabled = state.isAllAppsEnabled,
            amountOfEnabledApps = state.amountOfEnabledApps,
            isAppsFailure = state.isAppsFailure,
            isSearchEnabled = isSearchEnabled,
            searchQuery = searchQuery
        )
    }

    @Composable
    private fun ScreenContent(
        modifier: Modifier = Modifier,
        apps: List<AppWithRules>,
        isAllAppsEnabled: Boolean,
        amountOfEnabledApps: Int,
        isAppsFailure: Throwable?,
        isSearchEnabled: MutableState<Boolean>,
        searchQuery: MutableState<String>
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            topBar = {
                TopScreenPart(
                    isAllAppsEnabled = isAllAppsEnabled,
                    amountOfEnabledApps = amountOfEnabledApps,
                    isSearchEnabled = isSearchEnabled,
                    searchQuery = searchQuery
                )
            }
        ) { padding ->
            AppList(
                modifier = Modifier.padding(padding),
                apps = apps,
                isAppsFailure = isAppsFailure
            )
        }
    }

    @Composable
    private fun TopScreenPart(
        modifier: Modifier = Modifier,
        isAllAppsEnabled: Boolean,
        amountOfEnabledApps: Int,
        isSearchEnabled: MutableState<Boolean>,
        searchQuery: MutableState<String>
    ) {
        val context = LocalContext.current
        val searchJob = remember { mutableStateOf<Job?>(null) }

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
                if (isSearchEnabled.value) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium),
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clickable {
                                        isSearchEnabled.value = !isSearchEnabled.value
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
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isSearchEnabled.value = false
                            }
                        )
                    )
                } else {
                    Text(
                        text = context.getString(R.string.all_apps),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier
                                .size(34.dp)
                                .clickable {
                                    isSearchEnabled.value = !isSearchEnabled.value
                                    searchQuery.value = ""
                                    appListViewModel.onSearch(query = searchQuery.value)
                                }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.all_apps),
                    style = MaterialTheme.typography.titleLarge
                )
                Switch(
                    checked = isAllAppsEnabled,
                    onCheckedChange = { appListViewModel.switchAllApps() }
                )
            }
            Text(
                text = context.getString(R.string.all_apps_info),
                style = MaterialTheme.typography.titleSmall
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
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = amountOfEnabledApps.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    @Composable
    private fun AppList(
        modifier: Modifier = Modifier,
        apps: List<AppWithRules>,
        isAppsFailure: Throwable?,
    ) {
        val lazyListState = rememberLazyListState()
        when {
            isAppsFailure != null -> {

            }

            else -> {
                LazyColumn(modifier) {
                    items(apps.size, key = { item -> apps[item].app.packageName }) { item ->
                        AppItem(item = apps[item])
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
        var showAppSettings = remember { mutableStateOf(false) }

        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable {
                        showAppSettings.value = !showAppSettings.value
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    val map by appListViewModel.bitmapMap.collectAsState()
                    val imageBitmap = map[item.app.packageName]?.asImageBitmap()

                    // Check if the imageBitmap is null and display accordingly
                    // Display the image or a placeholder if the bitmap is null
                    if (imageBitmap != null) {
                        Image(
                            modifier = Modifier.size(34.dp),
                            bitmap = imageBitmap,
                            contentDescription = "",
                        )
                    } else {
                        Image(
                            modifier = Modifier.size(34.dp),
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = ""
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    Text(
                        text = item.app.name ?: item.app.packageName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.app.enabled) {
                        Icon(
                            modifier = Modifier.clickable {
                                navController.navigate(NavGraphs.EmergencyAccessGraph.route)
                            },
                            imageVector = Icons.Outlined.ReportProblem,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(
                        checked = item.app.enabled,
                        onCheckedChange = {
                            showAppSettings.value = !showAppSettings.value
                            if (item.app.enabled) {
                                appListViewModel.checkApp(item, !item.app.enabled)
                            }
                        }
                    )
                }
            }
            if (showAppSettings.value) {
                AppRuleManager(app = item, showAppSettings = showAppSettings)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppRuleManager(
        modifier: Modifier = Modifier,
        app: AppWithRules,
        showAppSettings: MutableState<Boolean>,
    ) {
        var timeLimitRule: Rule.TimeLimitRule? = null
        var hourOfTheDayRangeRule: Rule.HourOfTheDayRangeRule? = null

        for (rule in app.rules) {
            when (rule) {
                is Rule.HourOfTheDayRangeRule -> hourOfTheDayRangeRule = rule
                is Rule.TimeLimitRule -> timeLimitRule = rule
            }
        }

        val timePickerTimeLimitRuleState = rememberTimePickerState(
            initialHour = formatSecondsToTime(timeLimitRule?.limitInSeconds).split(":")[0].toInt(),
            initialMinute = formatSecondsToTime(timeLimitRule?.limitInSeconds).split(":")[1].toInt(),
            is24Hour = true
        )

        var showTimePickerTimeLimitRule by remember {
            mutableStateOf(false)
        }

        val timePickerStateFrom = rememberTimePickerState(
            initialHour = hourOfTheDayRangeRule?.fromHour ?: 0,
            initialMinute = hourOfTheDayRangeRule?.fromMinute ?: 0,
            is24Hour = true
        )
        var showTimePickerFrom by remember { mutableStateOf(false) }

        val timePickerStateTo = rememberTimePickerState(
            initialHour = hourOfTheDayRangeRule?.toHour ?: 0,
            initialMinute = hourOfTheDayRangeRule?.toMinute ?: 0,
            is24Hour = true
        )
        var showTimePickerTo by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                text = LocalContext.current.getString(R.string.about_time_limit_rule)
            )
            Row(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clickable { showTimePickerTimeLimitRule = !showTimePickerTimeLimitRule }
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (showTimePickerTimeLimitRule) {
                    Dialog(
                        onDismissRequest = {
                            showTimePickerTimeLimitRule = false
                            appListViewModel.setTimeLimitRule(
                                packageName = app.app.packageName,
                                enabled = showAppSettings.value,
                                hour = timePickerTimeLimitRuleState.hour,
                                minute = timePickerTimeLimitRuleState.minute
                            )
                            if (!app.app.enabled) {
                                appListViewModel.checkApp(app, !app.app.enabled)
                            }
                        }
                    ) {
                        TimePicker(state = timePickerTimeLimitRuleState)
                    }
                }
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatTime(
                        timePickerTimeLimitRuleState.hour,
                        timePickerTimeLimitRuleState.minute
                    ),
                    style = MaterialTheme.typography.titleLarge,
                )

            }
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                text = LocalContext.current.getString(R.string.about_hour_of_the_day_range_rule)
            )
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clickable { showTimePickerFrom = !showTimePickerFrom }
                        .weight(1f)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (showTimePickerFrom) {
                        Dialog(
                            onDismissRequest = {
                                showTimePickerFrom = false
                                if (!app.app.enabled) {
                                    appListViewModel.checkApp(app, !app.app.enabled)
                                }
                                // todo time picker 1
                                appListViewModel.setHourOfTheDayRangeRule(
                                    app.app.packageName,
                                    true,
                                    timePickerStateFrom.hour to timePickerStateFrom.minute,
                                    null,
                                )
                            }
                        ) {
                            TimePicker(state = timePickerStateFrom)
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formatTime(
                            timePickerStateFrom.hour,
                            timePickerStateFrom.minute
                        ),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .size(34.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clickable { showTimePickerTo = !showTimePickerTo }
                        .weight(1f)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (showTimePickerTo) {
                        Dialog(
                            onDismissRequest = {
                                showTimePickerTo = false
                                if (!app.app.enabled) {
                                    appListViewModel.checkApp(app, !app.app.enabled)
                                }
                                appListViewModel.setHourOfTheDayRangeRule(
                                    app.app.packageName,
                                    true,
                                    null,
                                    timePickerStateTo.hour to timePickerStateTo.minute,
                                )
                            }
                        ) {
                            TimePicker(state = timePickerStateTo)
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formatTime(timePickerStateTo.hour, timePickerStateTo.minute),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}
