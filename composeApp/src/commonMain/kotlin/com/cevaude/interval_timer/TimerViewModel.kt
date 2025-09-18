package com.cevaude.interval_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel : ViewModel() {

    private val timerManager = TimerManager(viewModelScope)

    val timerState: StateFlow<TimerState> = timerManager.timerState
    val finisherSoundEvent: StateFlow<Int> = timerManager.finisherSoundEvent

    fun startTestRoutine() {
        timerManager.startTestRoutine()
    }

    fun stopTimer() {
        timerManager.stopTimer()
    }

    fun proceedToNextStep() {
        timerManager.proceedToNextStep()
    }

}