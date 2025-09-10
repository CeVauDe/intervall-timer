package com.cevaude.interval_timer

object TrainingRoutineFactory {
    fun createComplexRoutine(): TrainingRoutine {
        return TrainingRoutine(
            steps = listOf(
                TrainingStep(TimerPhase.WARMUP, 25),
                TrainingStep(TimerPhase.TRAINING, 25),
                TrainingStep(TimerPhase.PAUSE, 25),
                TrainingStep(TimerPhase.TRAINING, 25),
                TrainingStep(TimerPhase.PAUSE, 25),
                TrainingStep(TimerPhase.COOLDOWN, 25)
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
