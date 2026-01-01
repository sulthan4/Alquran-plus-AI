package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerseSeparator(modifier: Modifier = Modifier) {
    Divider(modifier = modifier.padding(vertical = 8.dp))
}
