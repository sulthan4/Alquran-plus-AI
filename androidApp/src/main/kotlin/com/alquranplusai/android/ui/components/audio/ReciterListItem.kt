package com.alquranplusai.android.ui.components.audio

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ReciterListItem(
    reciterName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(reciterName) },
        modifier = modifier
    )
}
