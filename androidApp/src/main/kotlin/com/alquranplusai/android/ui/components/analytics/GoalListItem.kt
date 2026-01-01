package com.alquranplusai.android.ui.components.analytics

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GoalListItem(
    goalTitle: String,
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(goalTitle) },
        supportingContent = { LinearProgressIndicator(progress = progress) },
        modifier = modifier
    )
}
