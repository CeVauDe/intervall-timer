package com.cevaude.interval_timer

import android.content.Context
import android.media.MediaPlayer

class AndroidFinisherSoundPlayer(private val context: Context) : FinisherSoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun play() {
        // Release any previous player
        mediaPlayer?.release()
        // Use a bundled raw resource (e.g., R.raw.finisher_sound)
        mediaPlayer = MediaPlayer.create(context, R.raw.finisher_sound)
        mediaPlayer?.setOnCompletionListener { it.release() }
        mediaPlayer?.start()
    }
}

