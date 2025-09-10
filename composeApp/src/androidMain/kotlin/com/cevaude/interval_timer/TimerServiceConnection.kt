package com.cevaude.interval_timer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

/**
 * Helper class to manage connections to the TimerService
 */
class TimerServiceConnection(private val context: Context) {
    // The bound service instance
    private var timerService: TimerService? = null

    // Connection state
    var isConnected by mutableStateOf(false)
        private set

    // Service connection callback
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? TimerService.TimerBinder
            timerService = binder?.getService()
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerService = null
            isConnected = false
        }
    }

    // Start the timer service
    fun startService() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
        }
        context.startForegroundService(intent)
        bindService()
    }

    // Stop the timer service
    fun stopService() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_STOP
        }
        context.startService(intent)
        unbindService()
    }

    // Bind to the service
    fun bindService() {
        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    // Unbind from the service
    fun unbindService() {
        if (isConnected) {
            context.unbindService(connection)
            isConnected = false
        }
    }
}

/**
 * Composable that manages the timer service connection
 */
@Composable
fun rememberTimerServiceConnection(): TimerServiceConnection {
    val context = LocalContext.current
    val serviceConnection = remember { TimerServiceConnection(context) }

    // Automatically bind when entering composition and unbind when leaving
    DisposableEffect(context) {
        serviceConnection.bindService()
        onDispose {
            serviceConnection.unbindService()
        }
    }

    return serviceConnection
}
