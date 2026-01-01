package com.alquranplusai.android.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Quick stats row showing streak, ayahs read, and time spent
 * Matches DESIGN_SPEC.md design
 */
@Composable
fun QuickStatsRow(
    streakDays: Int,
    ayahsRead: Int,
    minutesSpent: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = "🔥",
            value = streakDays.toString(),
            label = "Days",
            modifier = Modifier.weight(1f)
        )
        
        StatCard(
            icon = "📖",
            value = ayahsRead.toString(),
            label = "Ayahs",
            modifier = Modifier.weight(1f)
        )
        
        StatCard(
            icon = "⏱️",
            value = minutesSpent.toString(),
            label = "min",
            modifier = Modifier.weight(1f)
        )
    }
}


