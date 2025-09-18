package com.cevaude.interval_timer

class TrainingRoutineBuilder {
    private var steps: MutableList<TrainingStep> = mutableListOf()

    fun build(): TrainingRoutine {
        return TrainingRoutine(steps)
    }

    fun addStep(name: String, durationSeconds: Int): TrainingRoutineBuilder {
        steps.add(TrainingStep(name, durationSeconds))
        return this
    }

    fun addRepeatingSteps(stepList: List<TrainingStep>, repetitions: Int): TrainingRoutineBuilder {
        for (i in 1..repetitions) {
            stepList.forEach { step ->
                steps.add(TrainingStep("${step.name} $i/$repetitions", step.durationSeconds))
            }
        }
        return this
    }
}