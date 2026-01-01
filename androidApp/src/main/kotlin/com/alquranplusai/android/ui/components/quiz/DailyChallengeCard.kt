package com.alquranplusai.android.ui.components.quiz

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DailyChallengeCard(
    challengeTitle: String,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Daily Challenge", style = MaterialTheme.typography.titleSmall)
                Text(text = challengeTitle, style = MaterialTheme.typography.titleMedium)
            }
            if (isCompleted) {
                Icon(
                    androidx.compose.material.icons.Icons.Default.CheckCircle,
                    "Completed",
                    tint = Color.Green
                )
            }
        }
    }
}
