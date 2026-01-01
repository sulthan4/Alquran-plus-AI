package com.alquranplusai.android.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FeatureGrid(
    onBrowseClick: () -> Unit,
    onAudioClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                title = "Browse Quran", 
                icon = Icons.Default.MenuBook, 
                gradient = Brush.horizontalGradient(listOf(Color(0xFF00838F), Color(0xFF00ACC1))),
                onClick = onBrowseClick,
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                title = "Audio Player", 
                icon = Icons.Default.PlayCircle, 
                gradient = Brush.horizontalGradient(listOf(Color(0xFF5E35B1), Color(0xFF7E57C2))),
                onClick = onAudioClick,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                title = "Bookmarks", 
                icon = Icons.Default.Bookmark, 
                gradient = Brush.horizontalGradient(listOf(Color(0xFFEF6C00), Color(0xFFFFA726))),
                onClick = onBookmarksClick,
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                title = "Daily Quiz", 
                icon = Icons.Default.EmojiEvents, 
                gradient = Brush.horizontalGradient(listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))),
                onClick = onQuizClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
