package com.alquranplusai.android.ui.components.common

import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        icon()
    }
}
