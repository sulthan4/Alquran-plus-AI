package com.alquranplusai.android.ui.components.quiz

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CountdownTimer(
    seconds: Int,
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        progress = seconds / 60f,
        modifier = modifier
    )
}
