package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimerViewModelTest {

    @Test
    fun initialState_isIdle() {
        val viewModel = TimerViewModel()
        
        assertEquals(TimerPhase.IDLE, viewModel.timerState.value.phase)
        assertEquals(0, viewModel.timerState.value.remainingTimeSeconds)
        assertFalse(viewModel.timerState.value.isRunning)
    }

    @Test
    fun startTimer_startsTrainingPhase() {
        val viewModel = TimerViewModel()
        
        viewModel.startTimer()
        
        assertEquals(TimerPhase.TRAINING, viewModel.timerState.value.phase)
        assertEquals(60, viewModel.timerState.value.remainingTimeSeconds)
        assertTrue(viewModel.timerState.value.isRunning)
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
}