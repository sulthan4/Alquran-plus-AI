package com.alquranplusai.android.ui.components.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().height(200.dp)) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text("Line Chart", style = MaterialTheme.typography.bodySmall)
        }
    }
}
