package ru.akurbanoff.apptracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.akurbanoff.apptracker.ui.navigation.MainNavigationGraph
import ru.akurbanoff.apptracker.ui.theme.AppTrackerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel.init()

        enableEdgeToEdge()
        setContent {
            AppTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigationGraph(Modifier.padding(innerPadding))
                }
            }
        }
        requestNotificationPermission()
    }

    private fun requestNotificationPermission(){
        if(Build.VERSION.SDK_INT >= 33 && ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                NOTIFICATION_REQUEST_CODE
            )
        }
    }

    companion object{
        private const val NOTIFICATION_REQUEST_CODE = 112
    }
}
