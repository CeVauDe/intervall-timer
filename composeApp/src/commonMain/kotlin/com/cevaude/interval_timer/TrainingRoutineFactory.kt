package com.cevaude.interval_timer

object TrainingRoutineFactory {
    fun createComplexRoutine(): TrainingRoutine {
        return TrainingRoutine(
            steps = listOf(
                TrainingStep(TimerPhase.WARMUP, 5),
                TrainingStep(TimerPhase.TRAINING, 5),
                TrainingStep(TimerPhase.PAUSE, 5),
                TrainingStep(TimerPhase.TRAINING, 5),
                TrainingStep(TimerPhase.PAUSE, 5),
                TrainingStep(TimerPhase.COOLDOWN, 5)
            )
        )
    }

    // Future expansion point for different routine types
    fun createShortRoutine(): TrainingRoutine {
        return TrainingRoutine(
            steps = listOf(
                TrainingStep(TimerPhase.TRAINING, 5),
                TrainingStep(TimerPhase.PAUSE, 5)
            )
        )
    }

    fun createCustomRoutine(steps: List<TrainingStep>): TrainingRoutine {
        return TrainingRoutine(steps)
    }
}
