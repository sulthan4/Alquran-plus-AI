package com.alquranplusai.android.ui.components.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalendarDay(
    hasStreak: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .padding(2.dp)
            .background(
                if (hasStreak) MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.small
            )
    )
}
