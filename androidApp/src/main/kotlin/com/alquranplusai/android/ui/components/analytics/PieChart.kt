package com.alquranplusai.android.ui.components.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    data: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.size(200.dp)) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text("Pie Chart", style = MaterialTheme.typography.bodySmall)
        }
    }
}
