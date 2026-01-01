package com.alquranplusai.android.ui.components.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionView(
    questionText: String,
    questionNumber: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Question $questionNumber of $totalQuestions",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = questionText, style = MaterialTheme.typography.titleLarge)
    }
}
