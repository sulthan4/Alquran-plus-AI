package com.alquranplusai.data.ai

/**
 * Text classification engine for Quran content
 */
class TextClassificationEngine {
    
    private val topicKeywords = mapOf(
        "prayer" to listOf("صلاة", "صلى", "prayer", "salah"),
        "faith" to listOf("إيمان", "آمن", "faith", "believe"),
        "charity" to listOf("زكاة", "صدقة", "charity", "zakat"),
        "paradise" to listOf("جنة", "paradise", "heaven"),
        "hell" to listOf("نار", "جهنم", "hell", "fire")
    )
    
    suspend fun classifyTopic(text: String): String {
        val lowerText = text.lowercase()
        for ((topic, keywords) in topicKeywords) {
            if (keywords.any { lowerText.contains(it.lowercase()) }) {
                return topic
            }
        }
        return "general"
    }
    
    suspend fun extractKeywords(text: String): List<String> {
        val words = text.split(" ", "،", ".", "؛")
        return words.filter { it.length > 3 }.take(10)
    }
}
