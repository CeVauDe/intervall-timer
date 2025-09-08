package com.cevaude.interval_timer

interface FinisherSoundPlayer {
    fun play()
}

object NoOpFinisherSoundPlayer : FinisherSoundPlayer {
    override fun play() { /* no-op */ }
}

