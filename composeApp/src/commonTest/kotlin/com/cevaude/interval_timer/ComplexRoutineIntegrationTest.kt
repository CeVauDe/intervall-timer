package com.cevaude.interval_timer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ComplexRoutineIntegrationTest {

    @Test
    fun completeRoutineFlow_progressesThroughAllSteps() = runTest {
        val viewModel = TimerViewModel()

        // Start the complex routine
        viewModel.startComplexRoutine()

        // Define expected complete flow
        val expectedFlow = listOf(
            Triple(TimerPhase.WARMUP, 0, 0.0f),
            Triple(TimerPhase.TRAINING, 1, 1.0f / 6.0f),
            Triple(TimerPhase.PAUSE, 2, 2.0f / 6.0f),
            Triple(TimerPhase.TRAINING, 3, 3.0f / 6.0f),
            Triple(TimerPhase.PAUSE, 4, 4.0f / 6.0f),
            Triple(TimerPhase.COOLDOWN, 5, 5.0f / 6.0f),
            Triple(TimerPhase.FINISHED, 6, 1.0f)
        )

        // Verify initial state (WARMUP)
        var state = viewModel.timerState.value
        assertEquals(TimerPhase.WARMUP, state.phase)
        assertEquals(5, state.remainingTimeSeconds)
        assertTrue(state.isRunning)
        assertEquals(0, state.currentStepIndex)
        assertEquals(6, state.totalSteps)
        assertEquals(0.0f, state.progressPercentage, 0.01f)

        // Progress through all phases
        for (i in 1 until expectedFlow.size) {
            viewModel.completeCurrentPhase()

            state = viewModel.timerState.value
            val (expectedPhase, expectedStepIndex, expectedProgress) = expectedFlow[i]

            assertEquals(expectedPhase, state.phase, "Wrong phase at step $i")
            assertEquals(expectedStepIndex, state.currentStepIndex, "Wrong step index at step $i")
            assertEquals(expectedProgress, state.progressPercentage, 0.01f, "Wrong progress at step $i")

            if (expectedPhase != TimerPhase.FINISHED) {
                assertTrue(state.isRunning, "Should be running at step $i")
                assertEquals(5, state.remainingTimeSeconds, "Wrong duration at step $i")
            } else {
                assertFalse(state.isRunning, "Should be finished at final step")
                assertEquals(0, state.remainingTimeSeconds, "Should have 0 time when finished")
            }
        }

        // Verify routine is still available for reference when finished
        assertNotNull(state.routine, "Routine should still be available when finished")
    }

    @Test
    fun routineFlow_maintainsCorrectStepSequence() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        val actualPhaseSequence = mutableListOf<TimerPhase>()

        // Collect all phases during progression
        actualPhaseSequence.add(viewModel.timerState.value.phase)

        repeat(6) {
            viewModel.completeCurrentPhase()
            actualPhaseSequence.add(viewModel.timerState.value.phase)
        }

        val expectedSequence = listOf(
            TimerPhase.WARMUP,
            TimerPhase.TRAINING,
            TimerPhase.PAUSE,
            TimerPhase.TRAINING,
            TimerPhase.PAUSE,
            TimerPhase.COOLDOWN,
            TimerPhase.FINISHED
        )

        assertEquals(expectedSequence, actualPhaseSequence)
    }

    @Test
    fun routineFlow_handlesStopAndRestart() = runTest {
        val viewModel = TimerViewModel()

        // Start routine and progress to training phase
        viewModel.startComplexRoutine()
        viewModel.completeCurrentPhase() // WARMUP -> TRAINING

        assertEquals(TimerPhase.TRAINING, viewModel.timerState.value.phase)
        assertEquals(1, viewModel.timerState.value.currentStepIndex)

        // Stop the timer
        viewModel.stopTimer()

        assertEquals(TimerPhase.IDLE, viewModel.timerState.value.phase)
        assertEquals(0, viewModel.timerState.value.remainingTimeSeconds)
        assertFalse(viewModel.timerState.value.isRunning)

        // Restart should begin from WARMUP again
        viewModel.startComplexRoutine()

        assertEquals(TimerPhase.WARMUP, viewModel.timerState.value.phase)
        assertEquals(0, viewModel.timerState.value.currentStepIndex)
        assertTrue(viewModel.timerState.value.isRunning)
    }

    @Test
    fun routineFlow_correctlyTracksProgress() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        // Test progress at key points
        val progressCheckpoints = listOf(
            0 to 0.0f,      // Start (WARMUP)
            2 to 2.0f/6.0f, // After first PAUSE
            4 to 4.0f/6.0f, // After second PAUSE
            6 to 1.0f       // Finished
        )

        for ((targetStep, expectedProgress) in progressCheckpoints) {
            // Progress to target step
            while (viewModel.timerState.value.currentStepIndex < targetStep) {
                viewModel.completeCurrentPhase()
            }

            val state = viewModel.timerState.value
            assertEquals(expectedProgress, state.progressPercentage, 0.01f,
                "Wrong progress at step $targetStep")
        }
    }
}
