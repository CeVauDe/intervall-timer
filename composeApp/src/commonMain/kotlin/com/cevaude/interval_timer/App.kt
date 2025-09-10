package com.cevaude.interval_timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(
    finisherSoundPlayer: FinisherSoundPlayer = NoOpFinisherSoundPlayer,
    onStartTimer: () -> Unit = {},
    onStopTimer: () -> Unit = {}
) {
    MaterialTheme {
        val viewModel: TimerViewModel = viewModel()
        val timerState by viewModel.timerState.collectAsState()
        val finisherSoundEvent by viewModel.finisherSoundEvent.collectAsState()

        // Play sound when finisherSoundEvent increments
        LaunchedEffect(finisherSoundEvent) {
            if (finisherSoundEvent > 0) {
                finisherSoundPlayer.play()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                // Phase name
                Text(
                    text = getPhaseDisplayName(timerState.phase),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Progress information for routine
                if (timerState.routine != null) {
                    Text(
                        text = "Step ${timerState.currentStepIndex + 1} of ${timerState.totalSteps}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Circular progress indicator with timer
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(200.dp)
                ) {
                    CircularProgress(
                        progress = if (timerState.routine != null) {
                            // Use routine progress for outer ring
                            timerState.progressPercentage
                        } else {
                            // Use phase progress for simple timer
                            getProgressPercentage(timerState.phase, timerState.remainingTimeSeconds)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Inner ring for current step progress (only for routines)
                    if (timerState.routine != null) {
                        CircularProgress(
                            progress = getProgressPercentage(timerState.phase, timerState.remainingTimeSeconds),
                            modifier = Modifier.size(160.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                        )
                    }

                    Text(
                        text = formatTime(timerState.remainingTimeSeconds),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Start/Stop/Reset button
                Button(
                    onClick = {
                        when (timerState.phase) {
                            TimerPhase.IDLE -> {
                                viewModel.startComplexRoutine()
                                onStartTimer()
                            }
                            TimerPhase.WARMUP, TimerPhase.TRAINING, TimerPhase.PAUSE, TimerPhase.COOLDOWN -> {
                                viewModel.stopTimer()
                                onStopTimer()
                            }
                            TimerPhase.FINISHED -> {
                                viewModel.stopTimer()
                                onStopTimer()
                            }
                        }
                    }
                ) {
                    Text(
                        text = getButtonText(timerState.phase),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 8f,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier) {
        drawCircularProgress(progress, strokeWidth, color)
    }
}

fun DrawScope.drawCircularProgress(
    progress: Float,
    strokeWidth: Float,
    color: Color
) {
    val diameter = size.minDimension
    val radius = diameter / 2f
    val insideRadius = radius - strokeWidth / 2f
    val topLeftOffset = (size.width - diameter) / 2f
    val topLeftOffset2 = (size.height - diameter) / 2f
    
    // Background circle
    drawCircle(
        color = color.copy(alpha = 0.2f),
        radius = insideRadius,
        center = center,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
    
    // Progress arc
    val sweepAngle = progress * 360f
    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        topLeft = androidx.compose.ui.geometry.Offset(topLeftOffset, topLeftOffset2),
        size = androidx.compose.ui.geometry.Size(diameter, diameter)
    )
}