package com.alquranplusai.android.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.ProfileViewModel
import com.alquranplusai.domain.models.Achievement
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val streak by viewModel.currentStreak.collectAsState()
    val readingTime by viewModel.totalReadingTime.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val goals by viewModel.goals.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // User Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = user?.username?.firstOrNull()?.uppercase() ?: "U",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            user?.username ?: "Guest User",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            user?.email ?: "Sign in to sync progress",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onNavigateToEditProfile) {
                        Icon(Icons.Default.Edit, "Edit Profile")
                    }
                }
            }

            // Stats Summary
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatBadge(
                            icon = Icons.Default.LocalFireDepartment,
                            value = "$streak days",
                            label = "Streak",
                            tint = Color(0xFFFF9800)
                        )
                        StatBadge(
                            icon = Icons.Default.Timer,
                            value = "${readingTime / 60}m",
                            label = "Read Time",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        StatBadge(
                            icon = Icons.Default.EmojiEvents,
                            value = "${achievements.size}",
                            label = "Awards",
                            tint = Color(0xFFFFD700)
                        )
                    }
                }
            }

            // Achievements
            item {
                Column {
                    SectionHeader("Achievements", "View All") {}
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (achievements.isEmpty()) {
                            item {
                                Text("No achievements yet. Keep reading!", style = MaterialTheme.typography.bodyMedium)
                            }
                        } else {
                            items(achievements) { achievement ->
                                AchievementCard(achievement)
                            }
                        }
                    }
                }
            }

            // Active Goals
            item {
                Column {
                    SectionHeader("Active Goals") {}
                    goals.forEach { goal ->
                        GoalItem(
                            title = goal.title,
                            progress = if (goal.target > 0) goal.current / goal.target.toFloat() else 0f,
                            target = "${goal.current}/${goal.target}"
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    if (goals.isEmpty()) {
                         Text(
                             "No active goals. Set a new goal!", 
                             style = MaterialTheme.typography.bodyMedium,
                             color = MaterialTheme.colorScheme.onSurfaceVariant
                         )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBadge(icon: ImageVector, value: String, label: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun SectionHeader(title: String, action: String? = null, onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (action != null) {
            TextButton(onClick = onAction) {
                Text(action)
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(modifier = Modifier.width(120.dp)) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                achievement.title,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1
            )
        }
    }
}

@Composable
fun GoalItem(title: String, progress: Float, target: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, style = MaterialTheme.typography.bodyMedium)
                Text(target, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape)
            )
        }
    }
}
