package ru.akurbanoff.apptracker.ui.emergency_access

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.view.ContextThemeWrapper
import android.widget.NumberPicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.viewinterop.AndroidView
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
        val expandWorkingTime = remember { mutableStateOf(0) }
        Scaffold(
            modifier = modifier.padding(horizontal = 16.dp),
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
                    enabled = expandWorkingTime.value != 0
                ) {
                    Text(text = LocalContext.current.getString(R.string.confirm))
                }
            }
        ) { padding ->
            Body(modifier = Modifier.padding(padding), expandWorkingTime = expandWorkingTime)
        }
    }

    @Composable
    fun TopBar(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = context.getString(R.string.emergency_access),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }

    @Composable
    fun Body(
        modifier: Modifier = Modifier,
        expandWorkingTime: MutableState<Int>
    ) {
        val context = LocalContext.current
        var notifyBeforeTime by remember { mutableStateOf("0") }
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
                }
                Column (
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                        .padding(start = 16.dp),
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
            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = context.getString(R.string.expand_for)
                )
                AndroidView(
                    factory = { context ->
                        val picker = NumberPicker(ContextThemeWrapper(context, R.style.AppTheme_Picker))
                        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        picker.minValue = 0
                        picker.maxValue = 60
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            picker.selectionDividerHeight = 0
                        }
                        picker.setOnValueChangedListener{ _, _, i2 ->
                            expandWorkingTime.value = i2
                            notifyBeforeTime = i2.toString()
                            if(vibrator.hasVibrator()) {
                                vibrator.vibrate(50)
                            }
                        }
                        picker
                    }
                )
                Text(
                    text = context.getString(R.string.minute)
                )
            }
        }
    }
}