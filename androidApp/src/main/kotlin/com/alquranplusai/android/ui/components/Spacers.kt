package com.alquranplusai.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.padding(vertical = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SpacerMedium() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SpacerLarge() {
    Spacer(modifier = Modifier.height(24.dp))
}
