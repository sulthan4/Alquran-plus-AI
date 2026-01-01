package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerseNumber(
    number: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
    ) {
        Text(
            text = number.toString(),
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
