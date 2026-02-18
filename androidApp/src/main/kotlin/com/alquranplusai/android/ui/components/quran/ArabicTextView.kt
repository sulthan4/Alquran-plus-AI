package com.alquranplusai.android.ui.components.quran

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.alquranplusai.android.ui.theme.ScheherazadeFontFamily

/**
 * A composable for rendering Arabic Quranic text with proper font and RTL direction.
 * Uses ScheherazadeFontFamily for authentic Quranic script rendering.
 */
@Composable
fun ArabicTextView(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Unspecified,
    contentDesc: String = text
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = ScheherazadeFontFamily,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = TextAlign.End,
            textDirection = TextDirection.Rtl,
            color = color
        ),
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = contentDesc }
    )
}
