package com.cevaude.interval_timer

data class TrainingStep(
    val phase: TimerPhase,
    val durationSeconds: Int
)

data class TrainingRoutine(
    val steps: List<TrainingStep>,
    val currentStepIndex: Int = 0
) {
    val currentStep: TrainingStep?
        get() = steps.getOrNull(currentStepIndex)

    val isComplete: Boolean
        get() = currentStepIndex >= steps.size

    fun nextStep(): TrainingRoutine =
        copy(currentStepIndex = currentStepIndex + 1)
}
