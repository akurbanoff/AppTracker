package ru.akurbanoff.apptracker.ui.service_settings

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.di.getApplicationComponent

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
        Column(modifier = modifier.padding(16.dp)) {
            TopScreenPart()
            Body()
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

    @Composable
    fun Body(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        var isNotificationsEnabled by remember { mutableStateOf(true) }
        var isControlInBrowserEnabled by remember { mutableStateOf(false) }

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
                Checkbox(
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
                Checkbox(
                    checked = isControlInBrowserEnabled,
                    onCheckedChange = {
                        isControlInBrowserEnabled = !isControlInBrowserEnabled
                    }
                )
            }
        }
    }
}
