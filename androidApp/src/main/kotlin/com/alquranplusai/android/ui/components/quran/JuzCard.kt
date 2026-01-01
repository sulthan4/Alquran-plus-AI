package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JuzCard(
    juzNumber: Int,
    startSurah: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Juz $juzNumber", style = MaterialTheme.typography.titleMedium)
            Text(text = "Starts: $startSurah", style = MaterialTheme.typography.bodySmall)
        }
    }
}
