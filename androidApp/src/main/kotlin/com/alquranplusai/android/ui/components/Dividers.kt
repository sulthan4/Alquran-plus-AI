package com.alquranplusai.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Divider Components
 * Various divider styles for the app
 */

@Composable
fun SpacerDivider(
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.height(16.dp))
}

@Composable
fun LineDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = 1.dp
    )
}

@Composable
fun ThickDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = 8.dp
    )
}
