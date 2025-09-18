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
    fun getButtonText_returnsCorrectText() {
        assertEquals("Start", getButtonText(TimerState()))
        assertEquals(
            "Stop", getButtonText(
                TimerState(
                    routine = TrainingRoutine(
                        steps = listOf(
                            TrainingStep("test", 2)
                        )
                    ), isRunning = true, remainingStepTimeSeconds = 1
                )
            )
        )
        assertEquals(
            "Reset", getButtonText(
                TimerState(
                    routine = TrainingRoutine(
                        steps = listOf(
                            TrainingStep("test", 2)
                        )
                    ), isRunning = true,
                )
            )
        )
    }

    @Test
    fun getProgressPercentage_calculatesCorrectProgress() {
        // Progress calculation: (totalSeconds - remainingSeconds) / totalSeconds

        // Step with 5 seconds total
        assertEquals(
            0.0f,
            getProgressPercentage(TrainingStep("test", 5), 5),
            0.01f
        )  // Just started
        assertEquals(
            0.6f,
            getProgressPercentage(TrainingStep("test", 5), 2),
            0.01f
        )  // 3 seconds elapsed
        assertEquals(1.0f, getProgressPercentage(TrainingStep("test", 5), 0), 0.01f)  // Finished

        // Step with 30 seconds total
        assertEquals(
            0.0f,
            getProgressPercentage(TrainingStep("test", 30), 30),
            0.01f
        )
        assertEquals(
            0.5f,
            getProgressPercentage(TrainingStep("test", 30), 15),
            0.01f
        )     // 3 seconds elapsed
    }
}