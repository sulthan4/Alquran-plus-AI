package com.alquranplusai.android.ui.components.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow
    )
    Row(modifier = modifier) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .background(color, MaterialTheme.shapes.small)
                    .clickable { onColorSelected(color) }
            )
        }
    }
}
