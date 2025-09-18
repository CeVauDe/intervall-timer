package com.cevaude.interval_timer



data class TrainingStep(
    val name: String,
    val durationSeconds: Int
)

data class TrainingRoutine(
    val steps: List<TrainingStep>,
    val totalDurationSeconds: Int = steps.sumOf { it.durationSeconds }
)

