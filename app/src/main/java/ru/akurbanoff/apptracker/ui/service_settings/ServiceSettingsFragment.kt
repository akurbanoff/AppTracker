package ru.akurbanoff.apptracker.ui.service_settings

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Vibrator
import android.view.ContextThemeWrapper
import android.widget.NumberPicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.akurbanoff.apptracker.R
import java.lang.reflect.Field


class ServiceSettingsFragment(
    private val navController: NavHostController,
) {

    private lateinit var serviceSettingsViewModel: ServiceSettingsViewModel

    @Composable
    fun Main() {
        serviceSettingsViewModel = hiltViewModel()
        val state = serviceSettingsViewModel.state.collectAsState()

        BackHandler {
            navController.popBackStack()
        }

        Content()
    }

    @Composable
    fun Content(modifier: Modifier = Modifier) {
        Scaffold(
            modifier = modifier.padding(horizontal = 16.dp),
            topBar = {
                TopScreenPart()
            }
        ) { padding ->
            Body(modifier = Modifier.padding(padding))
        }
    }

    @Composable
    fun TopScreenPart(modifier: Modifier = Modifier) {
        val context = LocalContext.current

        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = context.getString(R.string.service_settings),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Body(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        var isNotificationsEnabled by remember { mutableStateOf(true) }
        var isControlInBrowserEnabled by remember { mutableStateOf(false) }
        var isAccessibilityAccessEnabled by remember { mutableStateOf(true) }
        var notifyBeforeTime by remember {
            mutableStateOf("0")
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isNotificationsEnabled = !isNotificationsEnabled },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = context.getString(R.string.is_notification_enabled),
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = isNotificationsEnabled,
                    onCheckedChange = {
                        isNotificationsEnabled = !isNotificationsEnabled
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isControlInBrowserEnabled = !isControlInBrowserEnabled },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = context.getString(R.string.enable_control_using_app_in_browser),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Switch(
                    checked = isControlInBrowserEnabled,
                    onCheckedChange = {
                        isControlInBrowserEnabled = !isControlInBrowserEnabled
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isControlInBrowserEnabled = !isControlInBrowserEnabled },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = context.getString(R.string.enable_emergency_access),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Switch(
                    checked = isAccessibilityAccessEnabled,
                    onCheckedChange = {
                        isAccessibilityAccessEnabled = !isAccessibilityAccessEnabled
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)){
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = context.getString(R.string.notify_before_close_apps),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(
                        text = context.getString(R.string.before)
                    )
                    AndroidView(
                        factory = { context ->
                            val picker = NumberPicker(ContextThemeWrapper(context, R.style.AppTheme_Picker))
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            picker.minValue = 0
                            picker.maxValue = 15
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                picker.selectionDividerHeight = 0
                            }
                            picker.setOnValueChangedListener{ _, _, i2 ->
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
}
