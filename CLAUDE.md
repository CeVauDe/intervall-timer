# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

IntervalTimer is a Kotlin Multiplatform interval timer application targeting Android, iOS, and Desktop (JVM) using Compose Multiplatform for the UI. The app allows users to run training routines with timed steps and visual/audio feedback.

## Development Commands

### Build Commands
- Build Android debug APK: `./gradlew :composeApp:assembleDebug`
- Run desktop application: `./gradlew :composeApp:run`
- Build all targets: `./gradlew build`

### Testing
- Run all tests: `./gradlew allTests`
- Run common tests: `./gradlew :composeApp:testDebugUnitTest`
- Run JVM tests: `./gradlew jvmTest`
- Run iOS simulator tests: `./gradlew iosSimulatorArm64Test`

### Single Test Execution
To run a single test, use: `./gradlew jvmTest --tests "fully.qualified.TestClassName.testMethodName"`

## Architecture

### Multiplatform Structure
- `composeApp/src/commonMain/` - Shared code for all platforms
- `composeApp/src/androidMain/` - Android-specific implementations (includes TimerService for background operation)
- `composeApp/src/iosMain/` - iOS-specific implementations
- `composeApp/src/jvmMain/` - Desktop JVM-specific implementations
- `composeApp/src/commonTest/` - Shared test code

### Core Components

**Timer Architecture:**
- `TimerManager` - Platform-independent timer logic that manages countdown state and routine execution
- `TimerViewModel` - Wraps TimerManager for UI layer, provides StateFlows
- `TimerState` - Data class representing current timer state (routine, step, time remaining, progress)
- `TrainingRoutine` & `TrainingStep` - Define multi-step training sequences
- `TrainingRoutineBuilder` - Builder pattern for creating routines

**UI Layer:**
- `App.kt` - Main Compose UI with dual circular progress rings (outer for routine, inner for current step)
- Material3 design system throughout

**Platform-Specific:**
- Android: `TimerService` runs timer in background, `TimerServiceConnection` bridges UI to service
- `FinisherSoundPlayer` - Platform-specific sound playback (implemented per platform)

### State Management
- Uses Kotlin StateFlow for reactive state
- `TimerManager` holds state, exposes via StateFlow
- ViewModel collects and provides to UI
- State is immutable data classes, updated via `copy()`

## Development Approach

**Test-Driven Development:**
1. Write test
2. Run test to see it fail for expected reason
3. Implement minimal code to pass test
4. Run all tests to verify
5. Commit code
6. Refactor if necessary
7. Run tests again
8. Commit refactorings

**Principles:**
- Write as little code as possible to complete tasks
- Prefer editing existing files over creating new ones

## Package Structure

All code under `com.cevaude.interval_timer` package. Main classes are flat in package root (no sub-packages currently).

## Troubleshooting

### Invalid Timestamp Build Error
If encountering "Invalid Timestamp -2147483648000" during build, fix with:
```bash
touch composeApp/src/commonMain/composeResources/drawable
```

## Technology Stack

- Kotlin 2.2.10 (JVM 11 target)
- Compose Multiplatform 1.8.2
- Android Gradle Plugin 8.13.0
- Kotlinx Coroutines 1.10.2
- Material3 design
- Gradle with configuration cache enabled
