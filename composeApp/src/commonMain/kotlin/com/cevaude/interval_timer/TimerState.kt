package com.cevaude.interval_timer

enum class TimerPhase {
    IDLE,
    WARMUP,
    TRAINING,
    PAUSE,
    COOLDOWN,
    FINISHED
}

data class TimerState(
    val phase: TimerPhase = TimerPhase.IDLE,
    val remainingTimeSeconds: Int = 0,
    val isRunning: Boolean = false
)