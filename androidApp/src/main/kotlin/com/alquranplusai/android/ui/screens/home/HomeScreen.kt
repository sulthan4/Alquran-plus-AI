package com.alquranplusai.android.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.alquranplusai.android.ui.components.common.*
import com.alquranplusai.android.ui.theme.*
import com.alquranplusai.android.ui.viewmodels.HomeViewModel
import com.alquranplusai.domain.models.Surah
import org.koin.androidx.compose.koinViewModel

/**
 * HomeScreen implementing the locked design specification
 * Deep Teal (#006064) primary, Purple (#5E35B1) secondary, Gold (#FFB300) accent
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToReading: (Int, Int) -> Unit,
    onNavigateToSurahList: () -> Unit,
    onNavigateToAudio: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToDailyQuiz: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val lastReadPosition by viewModel.lastReadPosition.collectAsState()
    val lastReadUiState by viewModel.lastReadUiState.collectAsState()
    val dailyVerse by viewModel.dailyVerse.collectAsState()
    val userName by viewModel.userName.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            // Header with gradient (Teal → Purple)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(HeaderGradient)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.md)
                        .padding(top = Spacing.lg),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(Spacing.sm))
                        Text(
                            text = "AlQuran Plus AI",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                containerColor = Gold,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            // Continue Reading Card
            item {
                GradientCard(
                    gradient = ContinueReadingGradient,
                    onClick = {
                        lastReadPosition?.let { (surah, ayah) ->
                            onNavigateToReading(surah, ayah)
                        } ?: onNavigateToSurahList()
                    },
                    modifier = Modifier.padding(horizontal = Spacing.md)
                ) {
                    val state = lastReadUiState ?: HomeViewModel.LastReadUiState("Surah Al-Fatiha", 1, 1, 7)
                    
                    Text(
                        text = "Continue Reading",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    Text(
                        text = "${state.surahName}, Ayah ${state.ayahNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    Text(
                        text = "ﷺ",  // Placeholder for Arabic decoration/text
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(Spacing.md))
                    LinearProgressIndicator(
                        progress = { state.ayahNumber.toFloat() / state.totalAyahs.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                        color = Gold,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Text(
                        text = "Verse ${state.ayahNumber} of ${state.totalAyahs}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    StatCard(
                        icon = "🔥",
                        value = "7",
                        label = "Days",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "📖",
                        value = "12",
                        label = "Ayahs",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "⏱️",
                        value = "25",
                        label = "min",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Feature Grid (2x2)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        FeatureCard(
                            title = "Browse Quran",
                            icon = Icons.Default.MenuBook,
                            gradient = BrowseQuranGradient,
                            onClick = onNavigateToSurahList,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "Audio Player",
                            icon = Icons.Default.Headphones,
                            gradient = AudioPlayerGradient,
                            onClick = onNavigateToAudio,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        FeatureCard(
                            title = "Bookmarks",
                            icon = Icons.Default.Bookmark,
                            gradient = BookmarksGradient,
                            onClick = onNavigateToBookmarks,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "Daily Quiz",
                            icon = Icons.Default.EmojiEvents,
                            gradient = DailyQuizGradient,
                            onClick = onNavigateToDailyQuiz,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Today's Challenge
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = Elevation.medium)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.width(Spacing.md))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Daily Verse",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            val verseText = dailyVerse?.let { (surah, ayah) ->
                                "Surah ${surah.nameTransliteration}, Ayah $ayah"
                            } ?: "Loading daily verse..."
                            
                            Text(
                                text = verseText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        TextButton(
                            onClick = {
                                dailyVerse?.let { (surah, ayah) ->
                                    onNavigateToReading(surah.number, ayah)
                                }
                            }
                        ) {
                            Text("Read →")
                        }
                    }
                }
            }
        }
    }
}
