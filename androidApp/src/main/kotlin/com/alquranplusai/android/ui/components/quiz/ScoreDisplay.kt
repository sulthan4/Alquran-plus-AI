package com.alquranplusai.android.ui.components.quiz

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScoreDisplay(
    score: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Score: $score / $totalQuestions",
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}
