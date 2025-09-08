package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimerPhaseTest {

    @Test
    fun timerPhase_hasAllRequiredValues() {
        // Test that all required phases exist
        val phases = TimerPhase.values()

        assertTrue(phases.contains(TimerPhase.IDLE))
        assertTrue(phases.contains(TimerPhase.WARMUP))
        assertTrue(phases.contains(TimerPhase.TRAINING))
        assertTrue(phases.contains(TimerPhase.PAUSE))
        assertTrue(phases.contains(TimerPhase.COOLDOWN))
        assertTrue(phases.contains(TimerPhase.FINISHED))
    }

    @Test
    fun timerPhase_hasCorrectOrder() {
        // Test that phases have the expected ordinal values
        assertEquals(0, TimerPhase.IDLE.ordinal)
        assertEquals(1, TimerPhase.WARMUP.ordinal)
        assertEquals(2, TimerPhase.TRAINING.ordinal)
        assertEquals(3, TimerPhase.PAUSE.ordinal)
        assertEquals(4, TimerPhase.COOLDOWN.ordinal)
        assertEquals(5, TimerPhase.FINISHED.ordinal)
    }
}
