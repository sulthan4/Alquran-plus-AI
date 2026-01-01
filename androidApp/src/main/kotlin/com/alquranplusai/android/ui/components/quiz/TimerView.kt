package com.alquranplusai.android.ui.components.quiz

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimerView(
    timeRemaining: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Time: ${timeRemaining}s",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}
