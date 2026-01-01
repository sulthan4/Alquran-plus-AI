package com.alquranplusai.android.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.QuizResultsViewModel
import com.alquranplusai.domain.models.QuizResult
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    quizId: String, // Treating this as attemptId/resultId
    viewModel: QuizResultsViewModel = koinViewModel(),
    onNavigateHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(quizId) {
        viewModel.loadResult(quizId)
    }

    val result by viewModel.currentResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Quiz Result") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.Close, "Close") } }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            result?.let { quizResult ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(32.dp))
                    
                    // Score Ring
                    ScoreRing(score = quizResult.score, percentage = quizResult.percentage)
                    
                    Spacer(Modifier.height(32.dp))
                    
                    Text(
                        if (quizResult.isPassed) "Congratulations!" else "Keep Learning!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (quizResult.isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(Modifier.height(32.dp))
                    
                    // Stats Grid
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        StatItem("Correct", "${quizResult.correctCount}", Icons.Default.CheckCircle, Color.Green)
                        StatItem("Wrong", "${quizResult.wrongCount}", Icons.Default.Close, Color.Red)
                        StatItem("Time", "${quizResult.timeSpent}s", Icons.Default.Timer, MaterialTheme.colorScheme.secondary)
                    }
                    
                    Spacer(Modifier.weight(1f))
                    Spacer(Modifier.height(32.dp))
                    
                    Button(
                        onClick = onNavigateHome,
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("Finish")
                    }
                }
            } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Result not found")
            }
        }
    }
}

@Composable
fun ScoreRing(score: Int, percentage: Float) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 20.dp,
        )
        CircularProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.fillMaxSize(),
            color = if (percentage >= 70) Color(0xFF4CAF50) else Color(0xFFF44336),
            strokeWidth = 20.dp,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
             Text(
                 "${percentage.toInt()}%",
                 style = MaterialTheme.typography.displayMedium,
                 fontWeight = FontWeight.Bold
             )
             Text("Score: $score", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(32.dp))
        Spacer(Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
