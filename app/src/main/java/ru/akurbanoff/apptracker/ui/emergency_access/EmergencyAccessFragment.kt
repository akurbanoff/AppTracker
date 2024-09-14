package ru.akurbanoff.apptracker.ui.emergency_access

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.domain.model.Rule
import ru.akurbanoff.apptracker.ui.utils.LifeScreen
import ru.akurbanoff.apptracker.ui.utils.formatSecondsToTime
import ru.akurbanoff.apptracker.ui.utils.formatTime

class EmergencyAccessFragment(
    private val navController: NavHostController,
    //private val app: AppWithRules
) {

    private lateinit var viewModel: EmergencyAccessViewModel
    private var app: AppWithRules? = null

    @Composable
    fun Main(modifier: Modifier = Modifier) {
        viewModel = hiltViewModel<EmergencyAccessViewModel>()

        val state by viewModel.state.collectAsState()

        app = state.app

        BackHandler {
            navController.popBackStack()
        }

        LifeScreen(
            onCreate = {
                viewModel.getApps()
            }
        )

        Content(modifier = modifier)
    }

    @Composable
    private fun Content(modifier: Modifier = Modifier) {
        Scaffold(
            modifier = modifier.padding(16.dp),
            topBar = {
                TopBar()
            },
            bottomBar = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = MaterialTheme.shapes.large,
                    onClick = {
                        // update app state
                    },
                    enabled = false
                ) {
                    Text(text = LocalContext.current.getString(R.string.confirm))
                }
            }
        ) { padding ->
            Body(modifier = Modifier.padding(padding))
        }
    }

    @Composable
    fun TopBar(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = context.getString(R.string.emergency_access),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Body(modifier: Modifier = Modifier) {
        var timeLimitRule: Rule.TimeLimitRule? = null
        var hourOfTheDayRangeRule: Rule.HourOfTheDayRangeRule? = null

        if(app != null) {
            for (rule in app?.rules!!) {
                when (rule) {
                    is Rule.HourOfTheDayRangeRule -> hourOfTheDayRangeRule = rule
                    is Rule.TimeLimitRule -> timeLimitRule = rule
                }
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

        val map by viewModel.bitmapMap.collectAsState()
        val imageBitmap = map[app?.app?.packageName]?.asImageBitmap()

        Column(
            modifier = modifier.padding(top = 24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (imageBitmap != null) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        bitmap = imageBitmap,
                        contentDescription = "",
                    )
                    Column (
                        modifier = Modifier.fillMaxHeight(0.1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = app?.app?.name ?: "",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(text = app?.app?.packageName ?: "")
                    }
                }
            }
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
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
                            viewModel.setTimeLimitRule(
                                packageName = app?.app?.packageName ?: "",
                                enabled = true,
                                hour = timePickerTimeLimitRuleState.hour,
                                minute = timePickerTimeLimitRuleState.minute
                            )
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
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                text = LocalContext.current.getString(R.string.about_hour_of_the_day_range_rule)
            )
            Row(
                modifier = Modifier
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
                        .padding(horizontal = 6.dp)
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