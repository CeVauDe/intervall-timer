package com.cevaude.interval_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    private val _finisherSoundEvent = MutableStateFlow(0)
    val finisherSoundEvent: StateFlow<Int> = _finisherSoundEvent.asStateFlow()
    private var countdownJob: Job? = null

    fun startComplexRoutine() {
        val routine = TrainingRoutineFactory.createComplexRoutine()
        startRoutine(routine)
    }

    private fun startRoutine(routine: TrainingRoutine) {
        routine.currentStep?.let { step ->
            _timerState.value = TimerState(
                phase = step.phase,
                remainingTimeSeconds = step.durationSeconds,
                isRunning = true,
                routine = routine,
                currentStepIndex = routine.currentStepIndex,
                totalSteps = routine.steps.size
            )
            startCountdown()
        }
    }

    fun startTimer() {
        _timerState.value = TimerState(
            phase = TimerPhase.TRAINING,
            remainingTimeSeconds = 5,
            isRunning = true
        )
        startCountdown()
    }
    
    fun stopTimer() {
        countdownJob?.cancel()
        _timerState.value = TimerState()
    }
    
    fun completeCurrentPhase() {
        val currentState = _timerState.value
        val routine = currentState.routine

        if (routine != null) {
            // Routine-based progression
            proceedToNextStep()
        } else {
            // Legacy progression for backward compatibility
            when (currentState.phase) {
                TimerPhase.TRAINING -> {
                    _timerState.value = TimerState(
                        phase = TimerPhase.PAUSE,
                        remainingTimeSeconds = 5,
                        isRunning = true
                    )
                    startCountdown()
                }
                TimerPhase.PAUSE -> {
                    countdownJob?.cancel()
                    _timerState.value = TimerState(
                        phase = TimerPhase.FINISHED,
                        remainingTimeSeconds = 0,
                        isRunning = false
                    )
                }
                else -> { /* Do nothing for IDLE or FINISHED */ }
            }
        }
    }

    private fun proceedToNextStep() {
        val currentState = _timerState.value
        val routine = currentState.routine ?: return

        val nextRoutine = routine.nextStep()
        // Finisher sound event: always trigger when moving to next step (except initial start)
        _finisherSoundEvent.value += 1

        if (nextRoutine.isComplete) {
            // Routine finished
            countdownJob?.cancel()
            _timerState.value = TimerState(
                phase = TimerPhase.FINISHED,
                remainingTimeSeconds = 0,
                isRunning = false,
                routine = nextRoutine,
                currentStepIndex = nextRoutine.currentStepIndex,
                totalSteps = nextRoutine.steps.size
            )
        } else {
            // Continue to next step
            startRoutine(nextRoutine)
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_timerState.value.isRunning) {
                delay(1000)
                val currentState = _timerState.value
                val newTime = currentState.remainingTimeSeconds - 1
                
                if (newTime > 0) {
                    // Continue countdown
                    _timerState.value = currentState.copy(remainingTimeSeconds = newTime)
                } else {
                    // Time's up, move to next phase
                    completeCurrentPhase()
                    break
                }
            }
        }
    }
}