package com.alquranplusai.android.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.AnalyticsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main Analytics Screen showing overview statistics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(
    onGoalCreated: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: com.alquranplusai.android.ui.viewmodels.GoalsViewModel = koinViewModel()
) {
    var title by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("reading") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Goal") },
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
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = target,
                onValueChange = { target = it },
                label = { Text("Target (number)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    viewModel.createGoal(title, target.toIntOrNull() ?: 0, type)
                    onGoalCreated()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Goal")
            }
        }
    }
}

