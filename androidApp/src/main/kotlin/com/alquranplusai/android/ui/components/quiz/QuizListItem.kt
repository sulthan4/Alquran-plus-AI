package com.alquranplusai.android.ui.components.quiz

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuizListItem(
    quizTitle: String,
    questionCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(quizTitle) },
        supportingContent = { Text("$questionCount questions") },
        modifier = modifier
    )
}
