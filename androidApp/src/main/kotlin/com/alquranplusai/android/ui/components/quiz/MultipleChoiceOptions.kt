package com.alquranplusai.android.ui.components.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MultipleChoiceOptions(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = option in selectedOptions,
                    onCheckedChange = { onOptionToggled(option) }
                )
                Text(text = option)
            }
        }
    }
}
