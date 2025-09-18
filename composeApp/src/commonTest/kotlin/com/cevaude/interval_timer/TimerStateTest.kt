package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNull

class TimerStateTest {

    @Test
    fun timerState_progressPercentage_handlesNoRoutine() {
        val state = TimerState()

        assertEquals(0.0f, state.progressPercentage, 0.01f)
    }
}