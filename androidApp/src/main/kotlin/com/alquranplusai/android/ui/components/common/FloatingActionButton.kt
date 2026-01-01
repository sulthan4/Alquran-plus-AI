package com.alquranplusai.android.ui.components.common

import androidx.compose.material3.FloatingActionButton as MaterialFAB
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialFAB(
        onClick = onClick,
        modifier = modifier
    ) {
        icon()
    }
}
