package com.cevaude.interval_timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A foreground service that runs the interval timer when the app is in background
 * or the screen is locked.
 */
class TimerService : Service() {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "interval_timer_channel"
        private const val NOTIFICATION_ID = 1
        private const val WAKE_LOCK_TAG = "IntervalTimer:TimerWakeLock"

        // Action constants for service control
        const val ACTION_START = "com.cevaude.interval_timer.START"
        const val ACTION_STOP = "com.cevaude.interval_timer.STOP"
    }

    // Create a service-specific coroutine scope
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // The timer manager that handles the actual timer logic
    private lateinit var timerManager: TimerManager

    // Wake lock to keep CPU running
    private var wakeLock: PowerManager.WakeLock? = null

    // Binder to allow activity to connect to the service
    private val binder = TimerBinder()

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    // Function to get the timer manager
    fun getTimerManager(): TimerManager = timerManager

    override fun onCreate() {
        super.onCreate()

        // Initialize the timer manager with the service's coroutine scope
        timerManager = TimerManager(serviceScope)

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel()

        // Acquire wake lock to keep the CPU running when screen is off
        acquireWakeLock()

        // Start observing timer state changes to update the notification
        observeTimerState()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Start as a foreground service with notification
                startForeground(NOTIFICATION_ID, createNotification())

                // Start the timer
                timerManager.startComplexRoutine()
            }
            ACTION_STOP -> {
                // Stop the timer and service
                timerManager.stopTimer()
                stopSelf()
            }
        }

        // If service is killed, restart it with the last intent
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()

        // Release the wake lock
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }

        // Cancel all coroutines
        serviceScope.cancel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Interval Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Shows current timer status"
                setShowBadge(false)
                // Disable sound and vibration for the timer notification
                setSound(null, null)
                enableVibration(false)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        // Create an intent to open the app when the notification is tapped
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            packageManager.getLaunchIntentForPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE
        )

        // Get current timer state
        val state = timerManager.getCurrentState()
        val phase = state.phase.name
        val time = formatTimeForDisplay(state.remainingTimeSeconds)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Interval Timer - $phase")
            .setContentText("Remaining time: $time")
            .setSmallIcon(android.R.drawable.ic_media_play) // Replace with your app icon
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    private fun observeTimerState() {
        serviceScope.launch {
            timerManager.timerState.collectLatest {
                // Update notification with current timer state
                updateNotification()

                // If timer is finished, stop the service
                if (it.phase == TimerPhase.FINISHED) {
                    stopSelf()
                }
            }
        }
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            WAKE_LOCK_TAG
        ).apply {
            // Don't let CPU go to sleep while timer is running
            acquire()
        }
    }

    private fun formatTimeForDisplay(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }
}
