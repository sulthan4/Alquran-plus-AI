package com.alquranplusai.android.ui.components.ai

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * AI Insights Card component showing AI-generated insights
 */
@Composable
fun AIInsightsCard(
    insights: List<AIInsight>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    "AI Insights",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            
            insights.forEach { insight ->
                AIInsightItem(insight)
            }
        }
    }
}

@Composable
private fun AIInsightItem(insight: AIInsight) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            insight.icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                insight.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                insight.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (insight.confidence != null) {
                LinearProgressIndicator(
                    progress = { insight.confidence },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    "${(insight.confidence * 100).toInt()}% confidence",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

data class AIInsight(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.Lightbulb,
    val confidence: Float? = null
)

/**
 * Predefined AI insights for common topics
 */
object AIInsights {
    fun getTopicInsights(topics: List<String>): List<AIInsight> {
        return topics.map { topic ->
            AIInsight(
                title = topic,
                description = "This verse discusses themes of $topic",
                icon = Icons.Default.Category,
                confidence = 0.85f
            )
        }
    }
    
    fun getSimilarVersesInsight(count: Int): AIInsight {
        return AIInsight(
            title = "Similar Verses",
            description = "Found $count similar verses with related themes",
            icon = Icons.Default.FindInPage
        )
    }
    
    fun getContextInsight(context: String): AIInsight {
        return AIInsight(
            title = "Historical Context",
            description = context,
            icon = Icons.Default.History
        )
    }
}
