package com.alquranplusai.android.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    val streak by viewModel.readingStreak.collectAsState()
    val totalTime by viewModel.totalReadingTime.collectAsState()
    val completedSurahs by viewModel.completedSurahs.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0277BD), Color(0xFF673AB7))
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                        .padding(top = 48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
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
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 8.dp)
                    .size(64.dp),
                containerColor = Color(0xFFFFB300),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    Icons.Default.Search, 
                    contentDescription = "Search",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Continue Reading Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                        .clickable {
                            lastReadPosition?.let { (surah, ayah) ->
                                onNavigateToReading(surah, ayah)
                            } ?: onNavigateToSurahList()
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF006064))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Continue Reading",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(Spacing.xs))
                        val state = lastReadUiState ?: HomeViewModel.LastReadUiState("Al-Fatiha", 1, 1, 7)
                        Text(
                            text = if (lastReadUiState != null) "${state.surahName}, Ayah ${state.ayahNumber}" else "Start your journey",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "ﷺ", 
                            fontSize = 48.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Thin Yellow Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(1.5.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(state.ayahNumber.toFloat() / state.totalAyahs.toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFFFFB300), RoundedCornerShape(1.5.dp))
                            )
                        }
                        Spacer(modifier = Modifier.height(Spacing.sm))
                        Text(
                            text = "Verse ${state.ayahNumber} of ${state.totalAyahs}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val statBg = Color(0xFFF5F5F5)
                    StatCard(
                        icon = "🔥",
                        value = streak.toString(),
                        label = "Days",
                        backgroundColor = statBg,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "📖",
                        value = completedSurahs.toString(),
                        label = "Surahs",
                        backgroundColor = statBg,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "⏱️",
                        value = (totalTime / 60).toString(), // Convert seconds to minutes
                        label = "min",
                        backgroundColor = statBg,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Feature Grid (2x2)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureCard(
                            title = "Browse Quran",
                            icon = Icons.Default.MenuBook,
                            background = Color(0xFF00ACC1),
                            onClick = onNavigateToSurahList,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "Audio Player",
                            icon = Icons.Default.Headphones,
                            background = Color(0xFF7E57C2),
                            onClick = onNavigateToAudio,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureCard(
                            title = "Bookmarks",
                            icon = Icons.Default.Bookmark,
                            background = Color(0xFFFB8C00),
                            onClick = onNavigateToBookmarks,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureCard(
                            title = "Daily Challenge",
                            icon = Icons.Default.EmojiEvents,
                            background = Color(0xFF00B248),
                            onClick = onNavigateToDailyQuiz,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
