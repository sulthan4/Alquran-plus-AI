package com.alquranplusai.android.ui.screens.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Help & FAQ Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    navController: NavController
) {
    val faqItems = remember {
        listOf(
            FAQItem(
                "How do I download translations?",
                "Go to Settings > Translations, select your preferred translations, and tap the download button."
            ),
            FAQItem(
                "How do I use Hifz mode?",
                "Select an ayah, tap the Hifz mode button, configure your repeat settings, and start playing."
            ),
            FAQItem(
                "How does semantic search work?",
                "Our AI-powered search understands meaning, not just keywords. Search for concepts like 'patience' to find relevant verses."
            ),
            FAQItem(
                "How do I manage storage?",
                "Go to Settings > Storage to view and manage downloaded content."
            ),
            FAQItem(
                "Can I use the app offline?",
                "Yes! Download your preferred translations, tafsir, and audio for full offline access."
            ),
            FAQItem(
                "How do I change reciters?",
                "Tap the reciter name in the audio player to select from available reciters."
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & FAQ") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            "Find answers to common questions below. For more help, contact support.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
            
            items(faqItems) { faq ->
                FAQCard(faq)
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                Button(
                    onClick = { /* Contact support */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Email, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contact Support")
                }
            }
        }
    }
}

@Composable
fun FAQCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
            
            if (expanded) {
                Text(
                    faq.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class FAQItem(
    val question: String,
    val answer: String
)
