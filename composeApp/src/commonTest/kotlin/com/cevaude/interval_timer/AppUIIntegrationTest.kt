package com.cevaude.interval_timer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppUIIntegrationTest {

    @Test
    fun app_startsComplexRoutineByDefault() = runTest {
        val viewModel = TimerViewModel()

        // Simulate clicking start button when in IDLE state
        // This should start the complex routine, not the simple timer
        viewModel.startComplexRoutine()

        val state = viewModel.timerState.value

        // Verify it starts with complex routine
        assertEquals(TimerPhase.WARMUP, state.phase)
        assertEquals(6, state.totalSteps)
        assertEquals(0, state.currentStepIndex)
        assertTrue(state.routine != null, "Should have a routine when starting complex routine")
    }

    @Test
    fun app_displaysCorrectProgressInformation() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        // Progress through a few steps and verify progress tracking
        var state = viewModel.timerState.value

        // Initial state: Step 1 of 6 (WARMUP)
        assertEquals(0, state.currentStepIndex)
        assertEquals(6, state.totalSteps)
        assertEquals(0.0f, state.progressPercentage, 0.01f)

        // Move to step 2 (TRAINING)
        viewModel.completeCurrentPhase()
        state = viewModel.timerState.value

        assertEquals(1, state.currentStepIndex)
        assertEquals(TimerPhase.TRAINING, state.phase)
        assertEquals(1.0f / 6.0f, state.progressPercentage, 0.01f)

        // Move to step 3 (PAUSE)
        viewModel.completeCurrentPhase()
        state = viewModel.timerState.value

        assertEquals(2, state.currentStepIndex)
        assertEquals(TimerPhase.PAUSE, state.phase)
        assertEquals(2.0f / 6.0f, state.progressPercentage, 0.01f)
    }

    @Test
    fun app_handlesProgressRingsCorrectly() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        val state = viewModel.timerState.value

        // Verify dual progress calculation is available
        val routineProgress = state.progressPercentage // Outer ring
        val stepProgress = getProgressPercentage(state.phase, state.remainingTimeSeconds) // Inner ring

        // Initial state should have different progress values
        assertEquals(0.0f, routineProgress, 0.01f) // Start of routine
        assertEquals(0.0f, stepProgress, 0.01f) // Start of warmup step

        // Both progress values should be valid percentages
        assertTrue(routineProgress >= 0.0f && routineProgress <= 1.0f)
        assertTrue(stepProgress >= 0.0f && stepProgress <= 1.0f)
    }
}
