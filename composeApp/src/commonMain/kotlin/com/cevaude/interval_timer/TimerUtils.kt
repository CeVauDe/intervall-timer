package com.cevaude.interval_timer

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
}


fun getButtonText(timerState: TimerState): String {
    if (timerState.routine == null) {
        return "Start"
    }
    if (timerState.isRunning && !timerState.isRoutineComplete) {
        return "Stop"
    }
    return "Reset"
}

fun getProgressPercentage(currentStep: TrainingStep, remainingSeconds: Int): Float {
    return if (currentStep.durationSeconds > 0) {
        (currentStep.durationSeconds - remainingSeconds).toFloat() / currentStep.durationSeconds
    } else {
        1f
    }
}
