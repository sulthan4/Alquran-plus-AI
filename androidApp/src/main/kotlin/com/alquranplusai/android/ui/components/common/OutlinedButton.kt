package com.alquranplusai.android.ui.components.common

import androidx.compose.material3.OutlinedButton as MaterialOutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialOutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text)
    }
}
