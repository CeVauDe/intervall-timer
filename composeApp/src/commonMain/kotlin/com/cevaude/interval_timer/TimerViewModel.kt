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
    
    private var countdownJob: Job? = null
    
    fun startTimer() {
        _timerState.value = TimerState(
            phase = TimerPhase.TRAINING,
            remainingTimeSeconds = 60,
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
        when (currentState.phase) {
            TimerPhase.TRAINING -> {
                _timerState.value = TimerState(
                    phase = TimerPhase.PAUSE,
                    remainingTimeSeconds = 120,
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
    
    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_timerState.value.isRunning && _timerState.value.remainingTimeSeconds > 0) {
                delay(1000)
                val currentState = _timerState.value
                if (currentState.remainingTimeSeconds > 0) {
                    _timerState.value = currentState.copy(
                        remainingTimeSeconds = currentState.remainingTimeSeconds - 1
                    )
                } else {
                    // Time's up, move to next phase
                    completeCurrentPhase()
                    break
                }
            }
        }
    }
}