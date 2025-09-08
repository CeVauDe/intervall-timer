# Design: Complex Training Routine

## Current State Analysis

From examining the existing code, I found:
- Current `TimerPhase` enum has: IDLE, TRAINING, PAUSE, FINISHED
- Simple linear progression: TRAINING (60s) → PAUSE (120s) → FINISHED
- Phase transitions are hardcoded in `TimerViewModel.completeCurrentPhase()`
- Timer durations are hardcoded in the ViewModel

## New Requirements

Design a training routine with:
- Warmup phase
- 2x repetitions of: Training + Pause
- Cooldown phase

### Complete Sequence
1. WARMUP (duration TBD)
2. TRAINING (duration TBD) 
3. PAUSE (duration TBD)
4. TRAINING (duration TBD) - repeat #2
5. PAUSE (duration TBD) - repeat #3  
6. COOLDOWN (duration TBD)
7. FINISHED

## Design Approach

### 1. Enhanced TimerPhase Enum
Add new phases to support the complete routine:
```kotlin
enum class TimerPhase {
    IDLE,
    WARMUP,
    TRAINING, 
    PAUSE,
    COOLDOWN,
    FINISHED
}
```

### 2. Training Routine Definition
Create a data structure to represent the training sequence:

```kotlin
data class TrainingStep(
    val phase: TimerPhase,
    val durationSeconds: Int
)

data class TrainingRoutine(
    val steps: List<TrainingStep>,
    val currentStepIndex: Int = 0
) {
    val currentStep: TrainingStep? 
        get() = steps.getOrNull(currentStepIndex)
    
    val isComplete: Boolean 
        get() = currentStepIndex >= steps.size
    
    fun nextStep(): TrainingRoutine = 
        copy(currentStepIndex = currentStepIndex + 1)
}
```

### 3. Enhanced TimerState
Update TimerState to track the routine progress:

```kotlin
data class TimerState(
    val phase: TimerPhase = TimerPhase.IDLE,
    val remainingTimeSeconds: Int = 0,
    val isRunning: Boolean = false,
    val routine: TrainingRoutine? = null,
    val currentStepIndex: Int = 0,
    val totalSteps: Int = 0
) {
    val progressPercentage: Float
        get() = if (totalSteps > 0) currentStepIndex.toFloat() / totalSteps else 0f
}
```

### 4. Routine Factory
Create a factory to generate predefined routines:

```kotlin
object TrainingRoutineFactory {
    fun createComplexRoutine(): TrainingRoutine {
        return TrainingRoutine(
            steps = listOf(
                TrainingStep(TimerPhase.WARMUP, 300),    // 5 min warmup
                TrainingStep(TimerPhase.TRAINING, 1800), // 30 min training
                TrainingStep(TimerPhase.PAUSE, 600),     // 10 min pause
                TrainingStep(TimerPhase.TRAINING, 1800), // 30 min training  
                TrainingStep(TimerPhase.PAUSE, 600),     // 10 min pause
                TrainingStep(TimerPhase.COOLDOWN, 300)   // 5 min cooldown
            )
        )
    }
    
    // Future expansion point for different routine types
    fun createShortRoutine(): TrainingRoutine { /* ... */ }
    fun createCustomRoutine(steps: List<TrainingStep>): TrainingRoutine { /* ... */ }
}
```

### 5. Enhanced TimerViewModel
Update the ViewModel to handle routine-based progression:

```kotlin
class TimerViewModel : ViewModel() {
    
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    
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
    
    private fun proceedToNextStep() {
        val currentState = _timerState.value
        val routine = currentState.routine ?: return
        
        val nextRoutine = routine.nextStep()
        
        if (nextRoutine.isComplete) {
            // Routine finished
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
    
    // Update completeCurrentPhase to use routine progression
    fun completeCurrentPhase() {
        proceedToNextStep()
    }
}
```

## Implementation Strategy (TDD Approach)

### Phase 1: Foundation
1. **Test**: Write tests for new `TimerPhase` enum values
2. **Implement**: Add WARMUP and COOLDOWN to enum
3. **Test**: Write tests for `TrainingStep` and `TrainingRoutine` data classes
4. **Implement**: Create the data structures
5. **Test**: Write tests for `TrainingRoutineFactory`
6. **Implement**: Create factory with hardcoded complex routine

### Phase 2: State Management  
1. **Test**: Write tests for enhanced `TimerState` with routine tracking
2. **Implement**: Update TimerState data class
3. **Test**: Write tests for routine progression in ViewModel
4. **Implement**: Update TimerViewModel to handle routines

### Phase 3: Integration
1. **Test**: Write integration tests for complete routine flow
2. **Implement**: Update UI to display current phase and progress
3. **Test**: Write tests for all phase transitions
4. **Implement**: Ensure all transitions work correctly

## Future Extensibility Points

1. **Custom Routines**: Users can define their own step sequences
2. **Routine Persistence**: Save/load routines from storage
3. **Multiple Routine Types**: Different predefined routines (beginner, advanced, etc.)
4. **Dynamic Durations**: Allow runtime modification of step durations
5. **Conditional Steps**: Steps that depend on previous performance or user input
6. **Nested Routines**: Routines that contain sub-routines

## Benefits of This Design

1. **Separation of Concerns**: Routine definition separate from timer logic
2. **Testability**: Each component can be unit tested independently  
3. **Extensibility**: Easy to add new phases, routines, or step types
4. **Maintainability**: Clear data structures and single responsibility
5. **Flexibility**: Can handle arbitrary sequences as required
6. **Progress Tracking**: Built-in support for showing routine progress

## Next Steps

Start with Phase 1 implementation, beginning with tests for the new TimerPhase enum values and the TrainingStep/TrainingRoutine data classes.
