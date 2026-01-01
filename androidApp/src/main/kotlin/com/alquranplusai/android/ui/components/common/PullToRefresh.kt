package com.alquranplusai.android.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Simplified PullToRefresh component.
 * TODO: Implement proper pull-to-refresh when Material3 API is stable.
 */
@Composable
fun PullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    // Simplified implementation - just show content
    // Pull-to-refresh functionality can be added later
    Box(modifier = Modifier) {
        content()
    }
}
