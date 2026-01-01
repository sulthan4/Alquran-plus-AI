package com.alquranplusai.android.ui.components.analytics

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
fun MilestoneCard(
    milestoneName: String,
    isAchieved: Boolean,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = milestoneName, style = MaterialTheme.typography.titleMedium)
            if (isAchieved) {
                Icon(
                    androidx.compose.material.icons.Icons.Default.CheckCircle,
                    "Achieved",
                    tint = Color.Green
                )
            }
        }
    }
}
