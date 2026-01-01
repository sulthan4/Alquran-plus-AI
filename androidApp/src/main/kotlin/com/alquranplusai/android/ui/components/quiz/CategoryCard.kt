package com.alquranplusai.android.ui.components.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryCard(
    categoryName: String,
    quizCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = categoryName, style = MaterialTheme.typography.titleMedium)
            Text(text = "$quizCount quizzes", style = MaterialTheme.typography.bodySmall)
        }
    }
}
