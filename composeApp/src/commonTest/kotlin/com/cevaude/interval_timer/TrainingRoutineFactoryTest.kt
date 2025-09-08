package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TrainingRoutineFactoryTest {

    @Test
    fun createComplexRoutine_hasCorrectSequence() {
        val routine = TrainingRoutineFactory.createComplexRoutine()

        assertEquals(6, routine.steps.size)
        assertEquals(0, routine.currentStepIndex)
        assertFalse(routine.isComplete)
    }

    @Test
    fun createComplexRoutine_hasCorrectSteps() {
        val routine = TrainingRoutineFactory.createComplexRoutine()

        val expectedSteps = listOf(
            TrainingStep(TimerPhase.WARMUP, 5),
            TrainingStep(TimerPhase.TRAINING, 5),
            TrainingStep(TimerPhase.PAUSE, 5),
            TrainingStep(TimerPhase.TRAINING, 5),
            TrainingStep(TimerPhase.PAUSE, 5),
            TrainingStep(TimerPhase.COOLDOWN, 5)
        )

        assertEquals(expectedSteps, routine.steps)
    }

    @Test
    fun createComplexRoutine_startsWithWarmup() {
        val routine = TrainingRoutineFactory.createComplexRoutine()

        assertEquals(TimerPhase.WARMUP, routine.currentStep?.phase)
        assertEquals(5, routine.currentStep?.durationSeconds)
    }

    @Test
    fun createComplexRoutine_followsCorrectPhaseOrder() {
        val routine = TrainingRoutineFactory.createComplexRoutine()

        // Verify the complete sequence
        val phaseSequence = routine.steps.map { it.phase }
        val expectedSequence = listOf(
            TimerPhase.WARMUP,
            TimerPhase.TRAINING,
            TimerPhase.PAUSE,
            TimerPhase.TRAINING,  // second training
            TimerPhase.PAUSE,     // second pause
            TimerPhase.COOLDOWN
        )

        assertEquals(expectedSequence, phaseSequence)
    }
}
