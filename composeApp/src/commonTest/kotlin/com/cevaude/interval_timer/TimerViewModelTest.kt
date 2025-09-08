package com.cevaude.interval_timer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModelTest {

    @Test
    fun initialState_isIdle() {
        val viewModel = TimerViewModel()
        
        assertEquals(TimerPhase.IDLE, viewModel.timerState.value.phase)
        assertEquals(0, viewModel.timerState.value.remainingTimeSeconds)
        assertFalse(viewModel.timerState.value.isRunning)
        assertNull(viewModel.timerState.value.routine)
        assertEquals(0, viewModel.timerState.value.currentStepIndex)
        assertEquals(0, viewModel.timerState.value.totalSteps)
    }

    @Test
    fun startTimer_startsTrainingPhase() {
        val viewModel = TimerViewModel()
        
        viewModel.startTimer()
        
        assertEquals(TimerPhase.TRAINING, viewModel.timerState.value.phase)
        assertEquals(5, viewModel.timerState.value.remainingTimeSeconds)
        assertTrue(viewModel.timerState.value.isRunning)
        assertNull(viewModel.timerState.value.routine) // Simple timer doesn't use routines
    }

    @Test
    fun stopTimer_stopsAndResetsToIdle() {
        val viewModel = TimerViewModel()
        
        viewModel.startTimer()
        viewModel.stopTimer()
        
        assertEquals(TimerPhase.IDLE, viewModel.timerState.value.phase)
        assertEquals(0, viewModel.timerState.value.remainingTimeSeconds)
        assertFalse(viewModel.timerState.value.isRunning)
    }

    @Test
    fun startComplexRoutine_initializesWithWarmupPhase() = runTest {
        val viewModel = TimerViewModel()

        viewModel.startComplexRoutine()

        val state = viewModel.timerState.value
        assertEquals(TimerPhase.WARMUP, state.phase)
        assertEquals(5, state.remainingTimeSeconds)
        assertTrue(state.isRunning)
        assertNotNull(state.routine)
        assertEquals(0, state.currentStepIndex)
        assertEquals(6, state.totalSteps)
    }

    @Test
    fun startComplexRoutine_setsCorrectRoutine() = runTest {
        val viewModel = TimerViewModel()

        viewModel.startComplexRoutine()

        val state = viewModel.timerState.value
        val routine = state.routine
        assertNotNull(routine)
        assertEquals(6, routine.steps.size)
        assertEquals(TimerPhase.WARMUP, routine.steps[0].phase)
        assertEquals(TimerPhase.TRAINING, routine.steps[1].phase)
        assertEquals(TimerPhase.PAUSE, routine.steps[2].phase)
        assertEquals(TimerPhase.TRAINING, routine.steps[3].phase)
        assertEquals(TimerPhase.PAUSE, routine.steps[4].phase)
        assertEquals(TimerPhase.COOLDOWN, routine.steps[5].phase)
    }

    @Test
    fun routineProgression_warmupToFirstTraining() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        // Complete warmup phase
        viewModel.completeCurrentPhase()

        val state = viewModel.timerState.value
        assertEquals(TimerPhase.TRAINING, state.phase)
        assertEquals(5, state.remainingTimeSeconds)
        assertEquals(1, state.currentStepIndex)
        assertTrue(state.isRunning)
    }

    @Test
    fun routineProgression_completeSequence() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        // Progress through all phases
        val expectedPhases = listOf(
            TimerPhase.WARMUP,
            TimerPhase.TRAINING,
            TimerPhase.PAUSE,
            TimerPhase.TRAINING,
            TimerPhase.PAUSE,
            TimerPhase.COOLDOWN,
            TimerPhase.FINISHED
        )

        for (i in expectedPhases.indices) {
            val state = viewModel.timerState.value
            assertEquals(expectedPhases[i], state.phase, "Wrong phase at step $i")

            if (i < expectedPhases.size - 1) {
                assertTrue(state.isRunning, "Should be running at step $i")
                viewModel.completeCurrentPhase()
            } else {
                assertFalse(state.isRunning, "Should be finished at final step")
            }
        }
    }

    @Test
    fun routineProgression_progressPercentageUpdates() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        // Initial state: step 0 of 6
        assertEquals(0.0f, viewModel.timerState.value.progressPercentage, 0.01f)

        // After completing warmup: step 1 of 6
        viewModel.completeCurrentPhase()
        assertEquals(1.0f / 6.0f, viewModel.timerState.value.progressPercentage, 0.01f)

        // After completing first training: step 2 of 6
        viewModel.completeCurrentPhase()
        assertEquals(2.0f / 6.0f, viewModel.timerState.value.progressPercentage, 0.01f)
    }

    @Test
    fun routineProgression_finishedStateTracksRoutine() = runTest {
        val viewModel = TimerViewModel()
        viewModel.startComplexRoutine()

        // Complete all phases
        repeat(6) {
            viewModel.completeCurrentPhase()
        }

        val state = viewModel.timerState.value
        assertEquals(TimerPhase.FINISHED, state.phase)
        assertFalse(state.isRunning)
        assertEquals(0, state.remainingTimeSeconds)
        assertNotNull(state.routine) // Routine should still be present for reference
        assertEquals(6, state.currentStepIndex) // Should be at the end
    }
}