package com.alquranplusai.android.ui.components.common

import androidx.compose.material3.TextButton as MaterialTextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialTextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text)
    }
}
