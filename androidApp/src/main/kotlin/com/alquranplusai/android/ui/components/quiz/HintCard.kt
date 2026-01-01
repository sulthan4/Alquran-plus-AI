package com.alquranplusai.android.ui.components.quiz

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HintCard(
    hint: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(androidx.compose.material.icons.Icons.Default.Lightbulb, "Hint")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = hint, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
