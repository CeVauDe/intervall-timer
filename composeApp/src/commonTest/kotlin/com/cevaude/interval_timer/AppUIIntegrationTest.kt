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
        viewModel.startTestRoutine()

        val state = viewModel.timerState.value

        // Verify it starts with test routine
        assertTrue(state.routine != null, "Should have a routine when starting complex routine")
        assertEquals(4, state.routine.steps.size)
        assertEquals(state.currentStep!!.name,"Step 1")
        assertEquals(0, state.currentStepIndex)
    }

    @Test
    fun app_displaysCorrectProgressInformation() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startTestRoutine()

        // Progress through a few steps and verify progress tracking
        var state = viewModel.timerState.value

        // Initial state: Step 1 of 4 (Step 1)
        assertEquals(0, state.currentStepIndex)
        assertEquals(4, state.totalSteps)
        assertEquals(0.0f, state.progressPercentage, 0.01f)

        // Move to step 2
        viewModel.proceedToNextStep()
        state = viewModel.timerState.value

        assertEquals(1, state.currentStepIndex)
        assertEquals("Step 2", state.currentStep!!.name)
        assertEquals(1.0f / 4.0f, state.progressPercentage, 0.01f)

        // Move to step 3
        viewModel.proceedToNextStep()
        state = viewModel.timerState.value

        assertEquals(2, state.currentStepIndex)
        assertEquals("Step 3", state.currentStep!!.name)
        assertEquals(2.0f / 4.0f, state.progressPercentage, 0.01f)
    }

    @Test
    fun app_handlesProgressRingsCorrectly() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startTestRoutine()

        val state = viewModel.timerState.value

        // Verify dual progress calculation is available
        val routineProgress = state.progressPercentage // Outer ring
        val stepProgress = getProgressPercentage(state.currentStep!!, state.remainingStepTimeSeconds) // Inner ring

        // Initial state should have different progress values
        assertEquals(0.0f, routineProgress, 0.01f) // Start of routine
        assertEquals(0.0f, stepProgress, 0.01f) // Start of warmup step

        // Both progress values should be valid percentages
        assertTrue(routineProgress >= 0.0f && routineProgress <= 1.0f)
        assertTrue(stepProgress >= 0.0f && stepProgress <= 1.0f)
    }

    @Test
    fun app_playsFinisherSound_whenPhaseFinishes() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startTestRoutine()

        val initialSound = viewModel.finisherSoundEvent.value

        // Complete first step (step 1 -> step 2)
        viewModel.proceedToNextStep()
        assertEquals(initialSound + 1, viewModel.finisherSoundEvent.value, "Finisher sound should play after first phase")

        // Complete second step (step 2 -> step 3)
        viewModel.proceedToNextStep()
        assertEquals(initialSound + 2, viewModel.finisherSoundEvent.value, "Finisher sound should play after second phase")
    }
}
