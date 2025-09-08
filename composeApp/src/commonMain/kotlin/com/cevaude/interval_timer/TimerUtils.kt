package com.cevaude.interval_timer

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
}

fun getPhaseDisplayName(phase: TimerPhase): String {
    return when (phase) {
        TimerPhase.IDLE -> "Ready"
        TimerPhase.TRAINING -> "Training"
        TimerPhase.PAUSE -> "Pause"
        TimerPhase.FINISHED -> "Finished"
    }
}

fun getButtonText(phase: TimerPhase): String {
    return when (phase) {
        TimerPhase.IDLE -> "Start"
        TimerPhase.TRAINING, TimerPhase.PAUSE -> "Stop"
        TimerPhase.FINISHED -> "Reset"
    }
}

fun getProgressPercentage(phase: TimerPhase, remainingSeconds: Int): Float {
    return when (phase) {
        TimerPhase.TRAINING -> {
            val totalSeconds = 60
            remainingSeconds.toFloat() / totalSeconds.toFloat()
        }
        TimerPhase.PAUSE -> {
            val totalSeconds = 120
            remainingSeconds.toFloat() / totalSeconds.toFloat()
        }
        else -> 0.0f
    }
}