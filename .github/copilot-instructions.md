# COPILOT INSTRUCTIONS

This file provides guidance to GitHub COPILOT when working with code in this repository.

## Project Overview

IntervalTimer is a Kotlin Multiplatform project targeting Android, iOS, and Desktop (JVM) using Compose Multiplatform for the UI framework. The application displays a simple "Hello world!" interface and is configured for cross-platform development.

## Architecture

- **Multiplatform Structure**: Uses Kotlin Multiplatform with shared code in `commonMain` and platform-specific implementations in `androidMain`, `iosMain`, and `jvmMain`
- **UI Framework**: Compose Multiplatform with Material3 design system
- **Package Structure**: Code is organized under `com.cevaude.interval_timer` package
- **Platform Targets**: 
  - Android (API 24+, targeting API 36)
  - iOS (arm64 and simulator)
  - Desktop JVM

## Development approach
- Use a test-driven development approach to implement the app: 
  1. Write test
  2. run test to see it fail for the expected reason
  3. implement just the necessary code to make test pass
  4. run all tests to see them pass
  5. commit code
  6. refactor if necessary
  7. run tests to see them still pass
  8. commit
  9. start again with 1. and the next feature
- write as little code as possible to complete a given task

## Common Development Commands

### Build Commands
- Build Android debug APK: `./gradlew :composeApp:assembleDebug`
- Run desktop application: `./gradlew :composeApp:run`
- Build all targets: `./gradlew build`

### Testing
- Run all tests: `./gradlew test`
- Run common tests: `./gradlew :composeApp:testDebugUnitTest`

### Platform-Specific Development
- **Android**: Build configurations available in `composeApp/build.gradle.kts` with debug/release variants
- **iOS**: Framework target configured as static framework with base name "ComposeApp"
- **Desktop**: Main class is `com.cevaude.interval_timer.MainKt`, supports DMG, MSI, and DEB packaging

## Key Configuration Files

- `gradle/libs.versions.toml`: Centralized dependency and plugin version management
- `composeApp/build.gradle.kts`: Main application build configuration with multiplatform targets
- `gradle.properties`: Kotlin and Android build optimization settings (4GB Gradle heap, configuration cache enabled)

## Development Environment

- **Kotlin**: 2.2.10 with JVM 11 compatibility
- **Compose Multiplatform**: 1.8.2 with hot reload support (beta06)
- **Android Gradle Plugin**: 8.10.1
- **Build Tools**: Gradle with configuration cache and build cache enabled

## Troubleshooting

### Build Failures
- **Invalid Timestamp Error**: If you encounter "Invalid Timestamp -2147483648000" errors during build, this is typically due to corrupted file timestamps in the compose resources directory. Fix by running: `touch composeApp/src/commonMain/composeResources/drawable`
 
