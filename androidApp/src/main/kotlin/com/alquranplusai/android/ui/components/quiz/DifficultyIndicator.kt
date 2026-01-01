package com.alquranplusai.android.ui.components.quiz

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DifficultyIndicator(
    difficulty: String,
    modifier: Modifier = Modifier
) {
    val color = when (difficulty.lowercase()) {
        "easy" -> Color.Green
        "medium" -> Color.Yellow
        "hard" -> Color.Red
        else -> MaterialTheme.colorScheme.primary
    }
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = difficulty,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
