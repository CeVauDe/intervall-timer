package com.cevaude.interval_timer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "IntervalTimer",
    ) {
        App()
    }
}