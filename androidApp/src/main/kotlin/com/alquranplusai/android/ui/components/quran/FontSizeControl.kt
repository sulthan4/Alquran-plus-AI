package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FontSizeControl(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Font Size: ${fontSize.toInt()}")
        Slider(
            value = fontSize,
            onValueChange = onFontSizeChange,
            valueRange = 12f..32f
        )
    }
}
