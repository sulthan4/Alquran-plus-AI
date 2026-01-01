package com.alquranplusai.android.ui.components.search

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HighlightedText(
    text: String,
    query: String,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        val lowerText = text.lowercase()
        val lowerQuery = query.lowercase()
        var lastIndex = 0
        
        while (true) {
            val index = lowerText.indexOf(lowerQuery, lastIndex)
            if (index == -1) {
                append(text.substring(lastIndex))
                break
            }
            append(text.substring(lastIndex, index))
            withStyle(SpanStyle(background = MaterialTheme.colorScheme.primaryContainer)) {
                append(text.substring(index, index + query.length))
            }
            lastIndex = index + query.length
        }
    }
    
    Text(text = annotatedString, modifier = modifier)
}
