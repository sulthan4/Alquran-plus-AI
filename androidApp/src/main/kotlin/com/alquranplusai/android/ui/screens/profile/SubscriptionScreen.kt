package com.alquranplusai.android.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main Profile Screen
 */
@Composable
fun SubscriptionScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subscription") },
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
                text = "Premium Features",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("✓ Ad-free experience")
                    Text("✓ Offline access to all translations")
                    Text("✓ Advanced analytics")
                    Text("✓ Custom themes")
                    Text("✓ Priority support")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* TODO: Handle subscription */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Subscribe - $4.99/month")
            }
        }
    }
}
