package com.cevaude.interval_timer


data class TimerState(
    val remainingStepTimeSeconds: Int = 0,
    val passedTimeSeconds: Int = 0,
    val isRunning: Boolean = false,
    val routine: TrainingRoutine? = null,
    val currentStepIndex: Int = 0
) {
    val progressPercentage: Float
        get() = if (routine?.steps?.isNotEmpty() ?: false) currentStepIndex.toFloat() / routine.steps.size else 0f

    val currentStep: TrainingStep?
        get() = routine?.steps?.getOrNull(currentStepIndex)

    val isRoutineComplete: Boolean
        get() = routine != null && currentStepIndex >= (routine.steps.size - 1) && remainingStepTimeSeconds <= 0

    val totalSteps: Int
        get() = routine?.steps?.size ?: 0
}
