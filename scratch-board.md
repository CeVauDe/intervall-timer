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
1. WARMUP (5s) ✅ IMPLEMENTED
2. TRAINING (5s) ✅ IMPLEMENTED
3. PAUSE (5s) ✅ IMPLEMENTED
4. TRAINING (5s) - repeat #2 ✅ IMPLEMENTED
5. PAUSE (5s) - repeat #3 ✅ IMPLEMENTED
6. COOLDOWN (5s) ✅ IMPLEMENTED
7. FINISHED ✅ IMPLEMENTED

## Implementation Status

### ✅ COMPLETED - Phase 1: Foundation
1. **Enhanced TimerPhase Enum** ✅
   - Added WARMUP and COOLDOWN phases
   - Updated all exhaustive when expressions
   - All tests passing

2. **Training Routine Data Structures** ✅
   - `TrainingStep(phase, durationSeconds)` data class
   - `TrainingRoutine(steps, currentStepIndex)` data class
   - Progress tracking with `currentStep`, `isComplete`, `nextStep()`
   - Comprehensive unit tests

3. **TrainingRoutineFactory** ✅
   - `createComplexRoutine()` with 6-step sequence using 5s durations
   - Extension points for `createShortRoutine()` and `createCustomRoutine()`
   - Factory pattern for easy routine creation

4. **Updated UI Components** ✅
   - Enhanced `getPhaseDisplayName()` for new phases
   - Updated `getButtonText()` to handle all phases
   - Modified `getProgressPercentage()` for 5-second durations
   - Fixed existing tests to match new requirements

### 🚧 IN PROGRESS - Phase 2: State Management  
1. **Enhanced TimerState** - NEXT
   - Add routine tracking properties
   - Add progress calculation
   - Update existing TimerState for backward compatibility

2. **Enhanced TimerViewModel** - NEXT
   - Add routine-based progression logic
   - Implement `startComplexRoutine()`
   - Update phase transition logic

### 📋 TODO - Phase 3: Integration
1. **Integration Tests** - TODO
   - Complete routine flow testing
   - Phase transition verification

2. **UI Updates** - TODO
   - Display routine progress
   - Show current step information

## Design Approach

### 1. Enhanced TimerPhase Enum ✅
```kotlin
enum class TimerPhase {
    IDLE,
    WARMUP,      // ✅ Added
    TRAINING, 
    PAUSE,
    COOLDOWN,    // ✅ Added
    FINISHED
}
```

### 2. Training Routine Definition ✅
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

### 3. Enhanced TimerState - NEXT TO IMPLEMENT
Update TimerState to track the routine progress:

```kotlin
data class TimerState(
    val phase: TimerPhase = TimerPhase.IDLE,
    val remainingTimeSeconds: Int = 0,
    val isRunning: Boolean = false,
    val routine: TrainingRoutine? = null,        // ⬅️ ADD
    val currentStepIndex: Int = 0,               // ⬅️ ADD
    val totalSteps: Int = 0                      // ⬅️ ADD
) {
    val progressPercentage: Float                // ⬅️ ADD
        get() = if (totalSteps > 0) currentStepIndex.toFloat() / totalSteps else 0f
}
```

### 4. Routine Factory ✅
```kotlin
object TrainingRoutineFactory {
    fun createComplexRoutine(): TrainingRoutine {
        return TrainingRoutine(
            steps = listOf(
                TrainingStep(TimerPhase.WARMUP, 5),
                TrainingStep(TimerPhase.TRAINING, 5),
                TrainingStep(TimerPhase.PAUSE, 5),
                TrainingStep(TimerPhase.TRAINING, 5),
                TrainingStep(TimerPhase.PAUSE, 5),
                TrainingStep(TimerPhase.COOLDOWN, 5)
            )
        )
    }
}
```

### 5. Enhanced TimerViewModel - NEXT TO IMPLEMENT
Update the ViewModel to handle routine-based progression:

```kotlin
class TimerViewModel : ViewModel() {
    fun startComplexRoutine() {                 // ⬅️ ADD
        val routine = TrainingRoutineFactory.createComplexRoutine()
        startRoutine(routine)
    }
    
    private fun startRoutine(routine: TrainingRoutine) {  // ⬅️ ADD
        // Implementation needed
    }
    
    private fun proceedToNextStep() {           // ⬅️ ADD
        // Implementation needed  
    }
}
```

## Implementation Strategy (TDD Approach)

### ✅ COMPLETED Phase 1: Foundation
1. ✅ **Test**: Write tests for new `TimerPhase` enum values
2. ✅ **Implement**: Add WARMUP and COOLDOWN to enum
3. ✅ **Test**: Write tests for `TrainingStep` and `TrainingRoutine` data classes
4. ✅ **Implement**: Create the data structures
5. ✅ **Test**: Write tests for `TrainingRoutineFactory`
6. ✅ **Implement**: Create factory with hardcoded complex routine

### 🚧 CURRENT Phase 2: State Management  
1. **Test**: Write tests for enhanced `TimerState` with routine tracking
2. **Implement**: Update TimerState data class
3. **Test**: Write tests for routine progression in ViewModel
4. **Implement**: Update TimerViewModel to handle routines

### 📋 NEXT Phase 3: Integration
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

Continue with Phase 2 implementation: Enhanced TimerState with routine tracking properties and enhanced TimerViewModel with routine-based progression logic.
