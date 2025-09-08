package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNull

class TimerStateTest {

    @Test
    fun timerState_hasDefaultValues() {
        val timerState = TimerState()
        
        assertEquals(TimerPhase.IDLE, timerState.phase)
        assertEquals(0, timerState.remainingTimeSeconds)
        assertFalse(timerState.isRunning)
        assertNull(timerState.routine)
        assertEquals(0, timerState.currentStepIndex)
        assertEquals(0, timerState.totalSteps)
        assertEquals(0.0f, timerState.progressPercentage)
    }

    @Test
    fun timerState_canBeInitializedWithValues() {
        val timerState = TimerState(
            phase = TimerPhase.TRAINING,
            remainingTimeSeconds = 5,
            isRunning = true
        )
        
        assertEquals(TimerPhase.TRAINING, timerState.phase)
        assertEquals(5, timerState.remainingTimeSeconds)
        assertTrue(timerState.isRunning)
    }

    @Test
    fun timerState_withRoutine_tracksProgress() {
        val routine = TrainingRoutineFactory.createComplexRoutine()
        val state = TimerState(
            phase = TimerPhase.WARMUP,
            remainingTimeSeconds = 5,
            isRunning = true,
            routine = routine,
            currentStepIndex = 0,
            totalSteps = 6
        )

        assertEquals(TimerPhase.WARMUP, state.phase)
        assertEquals(5, state.remainingTimeSeconds)
        assertEquals(true, state.isRunning)
        assertEquals(routine, state.routine)
        assertEquals(0, state.currentStepIndex)
        assertEquals(6, state.totalSteps)
    }

    @Test
    fun timerState_progressPercentage_calculatesCorrectly() {
        val state = TimerState(
            currentStepIndex = 2,
            totalSteps = 6
        )

        assertEquals(2.0f / 6.0f, state.progressPercentage, 0.01f)
    }

    @Test
    fun timerState_progressPercentage_handlesZeroSteps() {
        val state = TimerState(
            currentStepIndex = 0,
            totalSteps = 0
        )

        assertEquals(0.0f, state.progressPercentage, 0.01f)
    }
}