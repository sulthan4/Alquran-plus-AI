package com.alquranplusai.android.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            ) 
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    onReadClick: () -> Unit,
    onListenClick: () -> Unit,
    onQuizClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickActionCard(
            title = "Read",
            icon = Icons.Default.MenuBook,
            onClick = onReadClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = "Listen",
            icon = Icons.Default.Headphones,
            onClick = onListenClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = "Quiz",
            icon = Icons.Default.Quiz,
            onClick = onQuizClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = "Bookmarks",
            icon = Icons.Default.Bookmark,
            onClick = onBookmarksClick,
            modifier = Modifier.weight(1f)
        )
    }
}
