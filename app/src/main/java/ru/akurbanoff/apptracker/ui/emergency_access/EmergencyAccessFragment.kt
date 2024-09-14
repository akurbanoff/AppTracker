package ru.akurbanoff.apptracker.ui.emergency_access

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.akurbanoff.apptracker.R
import ru.akurbanoff.apptracker.domain.model.AppWithRules
import ru.akurbanoff.apptracker.ui.utils.LifeScreen

class EmergencyAccessFragment(
    private val navController: NavHostController,
    private val app: AppWithRules
) {

    private lateinit var viewModel: EmergencyAccessViewModel

    @Composable
    fun Main(modifier: Modifier = Modifier) {
        viewModel = hiltViewModel<EmergencyAccessViewModel>()

        val state by viewModel.state.collectAsState()

        BackHandler {
            navController.popBackStack()
        }

        LifeScreen(
            onCreate = {
                viewModel.requestImageFor(app)
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
                text = app.app.name ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }

    @Composable
    fun Body(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        var timeLimit by remember { mutableStateOf("") }
        var hourOfTheDay by remember { mutableStateOf("") }
        val map by viewModel.bitmapMap.collectAsState()
        val imageBitmap = map[app.app.packageName]?.asImageBitmap()

        Column(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (imageBitmap != null) {
                    Image(
                        modifier = Modifier.size(34.dp),
                        bitmap = imageBitmap,
                        contentDescription = "",
                    )
                    Text(
                        text = app.app.name ?: "",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Text(
                    text = context.getString(R.string.about_time_limit_rule),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextField(
                    value = timeLimit,
                    onValueChange = { timeLimit = it },
                )
                Text(
                    text = context.getString(R.string.about_hour_of_the_day_range_rule),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextField(
                    value = hourOfTheDay,
                    onValueChange = { hourOfTheDay = it }
                )
            }
        }
    }
}