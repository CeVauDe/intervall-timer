package com.cevaude.interval_timer

import kotlin.test.Test
import kotlin.test.assertEquals

class NewTrainingRoutineTest {

    @Test
    fun trainingRoutineBuilder_createsEmptyRoutine() {
        val builder = TrainingRoutineBuilder()
        val routine = builder.build()

        assertEquals(0, routine.steps.size, message = "Empty routine should have zero steps")
    }

    @Test
    fun trainingRoutineBuilder_addsOneStepCorrectly() {
        val builder = TrainingRoutineBuilder()
        builder.addStep("Step 1", 30)
        val routine = builder.build()

        assertEquals(1, routine.steps.size, message = "Routine should have one step")
        assertEquals("Step 1", routine.steps[0].name, message = "Step name should match")
        assertEquals(30, routine.steps[0].durationSeconds, message = "Step duration should match")
    }

    @Test
    fun trainingRoutineBuilder_addsMultipleStepsCorrectly() {
        val builder = TrainingRoutineBuilder()
        for (i in 1..5) {
            builder.addStep("Step $i", i * 10)
        }
        val routine = builder.build()
        assertEquals(5, routine.steps.size, message = "Routine should have five steps")
    }
    
    @Test
    fun trainingRoutineBuilder_addsRepeatedStepsCorrectly() {
        val builder = TrainingRoutineBuilder()
        builder.addRepeatingSteps(listOf(TrainingStep("Run", 5), TrainingStep("Pause", 5),), 3)
        val routine = builder.build()

        assertEquals(6, routine.steps.size, message = "Routine should have six steps")
        for (i in 0..2){
            val iteration = i + 1
            assertEquals("Run $iteration/3", routine.steps[i*2].name, message = "Run step of iteration should match")
            assertEquals("Pause $iteration/3", routine.steps[i*2+1].name, message = "Pause step of iteration should match")
        }
    }

    @Test
    fun trainingRoutineBuilder_addsMixedStepsCorrectly() {
        val builder = TrainingRoutineBuilder()
        builder.addStep("Warmup", 15)
        builder.addRepeatingSteps(listOf(TrainingStep("Run", 5), TrainingStep("Pause", 5),), 2)
        builder.addStep("Cooldown", 10)
        val routine = builder.build()

        assertEquals(6, routine.steps.size, message = "Routine should have six steps")
    }

    @Test
    fun trainingRoutineBuilder_calculatesTotalDurationCorrectly() {
        val builder = TrainingRoutineBuilder()
        builder.addStep("Warmup", 15)
        builder.addStep("Cooldown", 10)
        val routine = builder.build()

        assertEquals(25, routine.totalDurationSeconds, message = "Total duration should be 25 seconds")
    }
}