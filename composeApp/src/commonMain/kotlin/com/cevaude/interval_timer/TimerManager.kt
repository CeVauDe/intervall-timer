package com.cevaude.interval_timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * A platform-independent timer manager that can be used by both the ViewModel and
 * platform-specific background services.
 */
class TimerManager(private val coroutineScope: CoroutineScope) {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _finisherSoundEvent = MutableStateFlow(0)
    val finisherSoundEvent: StateFlow<Int> = _finisherSoundEvent.asStateFlow()

    private var countdownJob: Job? = null

    fun startTestRoutine() {
        val builder = TrainingRoutineBuilder()
        builder.addStep("Step 1", 10)
        builder.addStep("Step 2", 10)
        builder.addStep("Step 3", 10)
        builder.addStep("Step 4", 10)

        startRoutine(builder.build())
    }

    fun startRoutine(routine: TrainingRoutine) {
        _timerState.value = TimerState(
            isRunning = true,
            routine = routine,
        )
        startStepCountdown()
    }

    fun stopTimer() {
        countdownJob?.cancel()
        _timerState.value = TimerState()
    }

    fun proceedToNextStep() {
        // Finisher sound event: always trigger when moving to next step (except initial start)
        _finisherSoundEvent.value += 1

        if (_timerState.value.isRoutineComplete) {
            // Routine finished
            countdownJob?.cancel()
            _timerState.value = TimerState()
        } else {
            // Continue to next step
            _timerState.value.currentStepIndex++
        }
    }

    private fun startStepCountdown() {
        _timerState.value.remainingStepTimeSeconds = _timerState.value.currentStep?.durationSeconds ?: 0

        countdownJob?.cancel()
        countdownJob = coroutineScope.launch {
            while (_timerState.value.isRunning) {
                delay(1000)
                val newTime = _timerState.value.remainingStepTimeSeconds - 1

                if (newTime > 0) {
                    // Continue countdown
                    _timerState.value.remainingStepTimeSeconds = newTime
                } else {
                    // Time's up, move to next step
                    proceedToNextStep()
                    break
                }
            }
        }
    }

    // Get the current state without using flows (useful for service implementations)
    fun getCurrentState(): TimerState {
        return _timerState.value
    }
}
