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
        // Training phase: 60 seconds total
        assertEquals(1.0f, getProgressPercentage(TimerPhase.TRAINING, 60), 0.01f)
        assertEquals(0.5f, getProgressPercentage(TimerPhase.TRAINING, 30), 0.01f)
        assertEquals(0.0f, getProgressPercentage(TimerPhase.TRAINING, 0), 0.01f)
        
        // Pause phase: 120 seconds total
        assertEquals(1.0f, getProgressPercentage(TimerPhase.PAUSE, 120), 0.01f)
        assertEquals(0.5f, getProgressPercentage(TimerPhase.PAUSE, 60), 0.01f)
        assertEquals(0.0f, getProgressPercentage(TimerPhase.PAUSE, 0), 0.01f)
        
        // Other phases
        assertEquals(0.0f, getProgressPercentage(TimerPhase.IDLE, 0), 0.01f)
        assertEquals(0.0f, getProgressPercentage(TimerPhase.FINISHED, 0), 0.01f)
    }
}