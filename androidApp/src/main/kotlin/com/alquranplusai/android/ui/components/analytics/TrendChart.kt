package com.alquranplusai.android.ui.components.analytics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TrendChart(
    trend: String,
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (percentage > 0) androidx.compose.material.icons.Icons.Default.TrendingUp 
            else androidx.compose.material.icons.Icons.Default.TrendingDown,
            "Trend"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$trend ${percentage.toInt()}%")
    }
}
