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
    val isRunning: Boolean = false,
    val routine: TrainingRoutine? = null,
    val currentStepIndex: Int = 0,
    val totalSteps: Int = 0
) {
    val progressPercentage: Float
        get() = if (totalSteps > 0) currentStepIndex.toFloat() / totalSteps else 0f
}
