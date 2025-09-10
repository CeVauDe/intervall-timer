package com.cevaude.interval_timer

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager

/**
 * Android-specific implementation of FinisherSoundPlayer that can work in both
 * the UI and background service.
 */
class AndroidFinisherSoundPlayer(private val context: Context) : FinisherSoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun play() {
        try {
            // Clean up any existing player
            mediaPlayer?.release()

            // Get the default notification sound
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // Create and play the media player
            mediaPlayer = MediaPlayer.create(context, notification).apply {
                setOnCompletionListener {
                    it.release()
                }
                start()
            }
        } catch (e: Exception) {
            // Log error if sound fails to play
            println("Error playing sound: ${e.message}")
        }
    }

    // Clean up resources
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
