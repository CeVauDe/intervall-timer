package com.cevaude.interval_timer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel : ViewModel() {
    
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    
    fun startTimer() {
        _timerState.value = TimerState(
            phase = TimerPhase.TRAINING,
            remainingTimeSeconds = 60,
            isRunning = true
        )
    }
    
    fun stopTimer() {
        _timerState.value = TimerState()
    }
}