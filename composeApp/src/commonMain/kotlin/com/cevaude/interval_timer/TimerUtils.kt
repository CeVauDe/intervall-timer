package com.cevaude.interval_timer

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
}

fun getPhaseDisplayName(phase: TimerPhase): String {
    return when (phase) {
        TimerPhase.IDLE -> "Ready"
        TimerPhase.WARMUP -> "Warmup"
        TimerPhase.TRAINING -> "Training"
        TimerPhase.PAUSE -> "Pause"
        TimerPhase.COOLDOWN -> "Cooldown"
        TimerPhase.FINISHED -> "Finished"
    }
}

fun getButtonText(phase: TimerPhase): String {
    return when (phase) {
        TimerPhase.IDLE -> "Start"
        TimerPhase.WARMUP, TimerPhase.TRAINING, TimerPhase.PAUSE, TimerPhase.COOLDOWN -> "Stop"
        TimerPhase.FINISHED -> "Reset"
    }
}

fun getProgressPercentage(phase: TimerPhase, remainingSeconds: Int): Float {
    return when (phase) {
        TimerPhase.WARMUP -> {
            val totalSeconds = 5
            (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
        }
        TimerPhase.TRAINING -> {
            val totalSeconds = 5
            (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
        }
        TimerPhase.PAUSE -> {
            val totalSeconds = 5
            (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
        }
        TimerPhase.COOLDOWN -> {
            val totalSeconds = 5
            (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
        }
        TimerPhase.IDLE, TimerPhase.FINISHED -> 0.0f
    }
}
