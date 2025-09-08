package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimerStateTest {

    @Test
    fun timerState_hasDefaultValues() {
        val timerState = TimerState()
        
        assertEquals(TimerPhase.IDLE, timerState.phase)
        assertEquals(0, timerState.remainingTimeSeconds)
        assertFalse(timerState.isRunning)
    }

    @Test
    fun timerState_canBeInitializedWithValues() {
        val timerState = TimerState(
            phase = TimerPhase.TRAINING,
            remainingTimeSeconds = 60,
            isRunning = true
        )
        
        assertEquals(TimerPhase.TRAINING, timerState.phase)
        assertEquals(60, timerState.remainingTimeSeconds)
        assertTrue(timerState.isRunning)
    }
}