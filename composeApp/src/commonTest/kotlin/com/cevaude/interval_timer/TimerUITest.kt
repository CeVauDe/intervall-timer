package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals

class TimerUITest {

    @Test
    fun formatTime_displaysCorrectMMSSFormat() {
        assertEquals("01:00", formatTime(60))
        assertEquals("02:00", formatTime(120))
        assertEquals("00:30", formatTime(30))
        assertEquals("00:05", formatTime(5))
        assertEquals("00:00", formatTime(0))
        assertEquals("10:15", formatTime(615))
    }

    @Test
    fun getPhaseDisplayName_returnsCorrectNames() {
        assertEquals("Training", getPhaseDisplayName(TimerPhase.TRAINING))
        assertEquals("Pause", getPhaseDisplayName(TimerPhase.PAUSE))
        assertEquals("Finished", getPhaseDisplayName(TimerPhase.FINISHED))
        assertEquals("Ready", getPhaseDisplayName(TimerPhase.IDLE))
    }

    @Test
    fun getButtonText_returnsCorrectText() {
        assertEquals("Start", getButtonText(TimerPhase.IDLE))
        assertEquals("Stop", getButtonText(TimerPhase.TRAINING))
        assertEquals("Stop", getButtonText(TimerPhase.PAUSE))
        assertEquals("Reset", getButtonText(TimerPhase.FINISHED))
    }

    @Test
    fun getProgressPercentage_calculatesCorrectProgress() {
        // All phases now use 5 seconds total duration
        // Progress calculation: (totalSeconds - remainingSeconds) / totalSeconds

        // Training phase: 5 seconds total
        assertEquals(0.0f, getProgressPercentage(TimerPhase.TRAINING, 5), 0.01f)  // Just started
        assertEquals(0.6f, getProgressPercentage(TimerPhase.TRAINING, 2), 0.01f)  // 3 seconds elapsed
        assertEquals(1.0f, getProgressPercentage(TimerPhase.TRAINING, 0), 0.01f)  // Finished

        // Pause phase: 5 seconds total
        assertEquals(0.0f, getProgressPercentage(TimerPhase.PAUSE, 5), 0.01f)     // Just started
        assertEquals(0.6f, getProgressPercentage(TimerPhase.PAUSE, 2), 0.01f)     // 3 seconds elapsed
        assertEquals(1.0f, getProgressPercentage(TimerPhase.PAUSE, 0), 0.01f)     // Finished

        // Warmup phase: 5 seconds total
        assertEquals(0.0f, getProgressPercentage(TimerPhase.WARMUP, 5), 0.01f)
        assertEquals(1.0f, getProgressPercentage(TimerPhase.WARMUP, 0), 0.01f)

        // Cooldown phase: 5 seconds total
        assertEquals(0.0f, getProgressPercentage(TimerPhase.COOLDOWN, 5), 0.01f)
        assertEquals(1.0f, getProgressPercentage(TimerPhase.COOLDOWN, 0), 0.01f)

        // Other phases
        assertEquals(0.0f, getProgressPercentage(TimerPhase.IDLE, 0), 0.01f)
        assertEquals(0.0f, getProgressPercentage(TimerPhase.FINISHED, 0), 0.01f)
    }
}