package com.cevaude.interval_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel : ViewModel() {

    private val timerManager = TimerManager(viewModelScope)

    val timerState: StateFlow<TimerState> = timerManager.timerState
    val finisherSoundEvent: StateFlow<Int> = timerManager.finisherSoundEvent

    fun startComplexRoutine() {
        timerManager.startComplexRoutine()
    }

    fun startTimer() {
        timerManager.startTimer()
    }

    fun stopTimer() {
        timerManager.stopTimer()
    }

    // For testing
    fun completeCurrentPhase() {
        timerManager.completeCurrentPhase()
    }
}