package com.cevaude.interval_timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Create the Android-specific sound player
        val soundPlayer = AndroidFinisherSoundPlayer(this)

        setContent {
            // This composable handles the service connection
            val serviceConnection = rememberTimerServiceConnection()

            // Main app content with callbacks for service control
            App(
                finisherSoundPlayer = soundPlayer,
                onStartTimer = {
                    // Start the background service when timer starts
                    serviceConnection.startService()
                },
                onStopTimer = {
                    // Stop the service when timer stops
                    serviceConnection.stopService()
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(finisherSoundPlayer = NoOpFinisherSoundPlayer)
}