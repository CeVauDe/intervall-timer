package com.cevaude.interval_timer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TimerCountdownTest {

    @Test
    fun startTimer_startsTrainingPhaseWithCorrectTime() = runTest {
        val viewModel = TimerViewModel()
        
        viewModel.startTimer()
        
        // Initial state should be training with 5 seconds
        assertEquals(TimerPhase.TRAINING, viewModel.timerState.value.phase)
        assertEquals(5, viewModel.timerState.value.remainingTimeSeconds)
        assertTrue(viewModel.timerState.value.isRunning)
    }

    @Test
    fun trainingPhase_transitionsToPausePhase() = runTest {
        val viewModel = TimerViewModel()
        
        // Start timer and wait for training phase to complete
        viewModel.startTimer()
        
        // Manually complete training phase
        viewModel.completeCurrentPhase()
        
        // Should now be in pause phase with 5 seconds
        assertEquals(TimerPhase.PAUSE, viewModel.timerState.value.phase)
        assertEquals(5, viewModel.timerState.value.remainingTimeSeconds)
        assertTrue(viewModel.timerState.value.isRunning)
    }

    @Test
    fun pausePhase_transitionsToFinished() = runTest {
        val viewModel = TimerViewModel()
        
        // Start and move through phases
        viewModel.startTimer()
        viewModel.completeCurrentPhase() // Training -> Pause
        viewModel.completeCurrentPhase() // Pause -> Finished
        
        // Should be finished
        assertEquals(TimerPhase.FINISHED, viewModel.timerState.value.phase)
        assertEquals(0, viewModel.timerState.value.remainingTimeSeconds)
        assertFalse(viewModel.timerState.value.isRunning)
    }
}