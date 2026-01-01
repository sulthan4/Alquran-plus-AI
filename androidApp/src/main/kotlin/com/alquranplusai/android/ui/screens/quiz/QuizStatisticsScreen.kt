package com.alquranplusai.android.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuizStatisticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: com.alquranplusai.android.ui.viewmodels.QuizViewModel = koinViewModel()
) {
    val completedQuizzes by viewModel.completedQuizzes.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Statistics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Total Quizzes Completed: ${completedQuizzes.size}",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Average Score: 85%")
            Text("Best Score: 100%")
            Text("Total Questions Answered: 250")
        }
    }
}

