package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class TrainingStepTest {

    @Test
    fun trainingStep_createsCorrectly() {
        val step = TrainingStep(TimerPhase.TRAINING, 5)

        assertEquals(TimerPhase.TRAINING, step.phase)
        assertEquals(5, step.durationSeconds)
    }
}

class TrainingRoutineTest {

    @Test
    fun trainingRoutine_currentStep_returnsFirstStepInitially() {
        val steps = listOf(
            TrainingStep(TimerPhase.WARMUP, 5),
            TrainingStep(TimerPhase.TRAINING, 5)
        )
        val routine = TrainingRoutine(steps)

        assertEquals(TrainingStep(TimerPhase.WARMUP, 5), routine.currentStep)
        assertEquals(0, routine.currentStepIndex)
    }

    @Test
    fun trainingRoutine_currentStep_returnsNullForEmptySteps() {
        val routine = TrainingRoutine(emptyList())

        assertNull(routine.currentStep)
    }

    @Test
    fun trainingRoutine_isComplete_falseWhenStepsRemain() {
        val steps = listOf(
            TrainingStep(TimerPhase.WARMUP, 5),
            TrainingStep(TimerPhase.TRAINING, 5)
        )
        val routine = TrainingRoutine(steps, currentStepIndex = 0)

        assertFalse(routine.isComplete)
    }

    @Test
    fun trainingRoutine_isComplete_trueWhenAllStepsFinished() {
        val steps = listOf(
            TrainingStep(TimerPhase.WARMUP, 5),
            TrainingStep(TimerPhase.TRAINING, 5)
        )
        val routine = TrainingRoutine(steps, currentStepIndex = 2)

        assertTrue(routine.isComplete)
    }

    @Test
    fun trainingRoutine_nextStep_advancesToNextStep() {
        val steps = listOf(
            TrainingStep(TimerPhase.WARMUP, 5),
            TrainingStep(TimerPhase.TRAINING, 5)
        )
        val routine = TrainingRoutine(steps, currentStepIndex = 0)

        val nextRoutine = routine.nextStep()

        assertEquals(1, nextRoutine.currentStepIndex)
        assertEquals(TrainingStep(TimerPhase.TRAINING, 5), nextRoutine.currentStep)
    }

    @Test
    fun trainingRoutine_nextStep_canAdvanceBeyondLastStep() {
        val steps = listOf(
            TrainingStep(TimerPhase.WARMUP, 5)
        )
        val routine = TrainingRoutine(steps, currentStepIndex = 0)

        val nextRoutine = routine.nextStep()

        assertEquals(1, nextRoutine.currentStepIndex)
        assertTrue(nextRoutine.isComplete)
        assertNull(nextRoutine.currentStep)
    }
}
