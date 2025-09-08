# Interval Timer Implementation Plan

## Overview
Transform the current "Hello World" app into a minimalistic interval timer that runs the following hardcoded sequence:
1. **Training Phase**: 1 minute
2. **Pause Phase**: 2 minutes  
3. **Finish**: Training complete

## Current State Analysis
- ✅ KMM Compose app with Material3 design system
- ✅ ViewModel and lifecycle dependencies already available
- ✅ Coroutines support (kotlinx.coroutinesSwing for desktop)
- ✅ Simple centered UI layout in `App.kt`

## Implementation Tasks

### Phase 1: Core Timer Logic
1. **Create Timer State Management**
   - Define timer states: `IDLE`, `TRAINING`, `PAUSE`, `FINISHED`
   - Create ViewModel to manage timer state and remaining time
   - Implement countdown logic using coroutines

2. **Create Timer Service/Repository**
   - Hardcode interval durations (1 min training, 2 min pause)
   - Handle state transitions between intervals
   - Emit time updates every second

### Phase 2: UI Components
3. **Update App.kt with Timer UI**
   - Replace "Hello World" with timer interface
   - Add start/stop button
   - Display current interval name ("Training" / "Pause" / "Finished")
   - Display remaining time in MM:SS format

4. **Design Minimalistic Layout**
   - Center all content vertically
   - Large, readable timer display
   - Clear interval status text
   - Single prominent action button

### Phase 3: Integration & Polish
5. **Connect ViewModel to UI**
   - Observe timer state in Compose
   - Handle button clicks to start/stop timer
   - Update UI reactively based on state changes

6. **Handle Timer Lifecycle**
   - Ensure timer continues in background (mobile considerations)
   - Handle app pause/resume gracefully
   - Reset functionality after completion

## Open Questions & Decisions Needed

### Technical Decisions
- **Timer Precision**: Should we use `Timer`, `CoroutineTimer`, or `flow` with delay for countdown?
    -> use flow from kotlinx.coroutines
- **Background Behavior**: How should the timer behave when app goes to background on mobile?
    -> The timer should continue to run
- **State Persistence**: Should timer state survive app restarts, or reset on app launch?
    -> The timer does not need to survive. We can launch with a fresh app

### UX Decisions  
- **Button Behavior**: 
  - Single button that changes text (Start → Stop → Reset)?
    -> yes
- **Audio/Visual Feedback**: 
  - Should there be sound notifications when intervals change?
    -> not for now
  - Visual indication (color changes, animations) for different phases?
    -> not for now
- **Timer Display**: 
  - Show total session time or just current interval time?
    -> just current interval
  - Display progress indicator (circular/linear)?
    -> circular indicator with timer digits centered in the circle

### Platform Considerations
- **Desktop**: Timer should work normally, no special considerations
- **Mobile (Android)**: May need foreground service for reliable background timing
- **iOS**: Background execution limitations to consider
  -> ignore iOS for now

## Files to Modify/Create

### Existing Files to Modify
- `composeApp/src/commonMain/kotlin/com/cevaude/interval_timer/App.kt` - Main UI update
- Potentially add dependencies to `composeApp/build.gradle.kts` if needed

### New Files to Create
- `TimerViewModel.kt` - State management and timer logic
- `TimerState.kt` - Data classes for timer state
- `IntervalTimer.kt` - Core timer business logic
- Potentially platform-specific timer implementations if needed

## Success Criteria
- [] App displays current interval ("Training", "Pause", or "Finished")  
- [] App shows remaining time in current interval (MM:SS format)
- [] Single button starts the hardcoded training sequence
- [] Timer automatically progresses: Training (1 min) → Pause (2 min) → Finished
- [] UI updates every second during active timing
- [] Clean, minimalistic design consistent with Material3
- [] Works on both desktop and mobile platforms

## Next Steps
1. Start with Phase 1: Create basic timer state management and ViewModel
2. Implement hardcoded intervals and countdown logic  
3. Build minimalistic UI to display timer state
4. Test on both desktop and mobile to ensure cross-platform compatibility

---
*This plan focuses on the MVP implementation. Future enhancements (custom intervals, sound, persistence) are intentionally out of scope.*