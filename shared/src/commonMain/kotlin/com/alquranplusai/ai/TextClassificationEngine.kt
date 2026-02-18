package com.alquranplusai.ai

/**
 * Classifies Quranic text into thematic categories using weighted keyword matching.
 * Returns confidence scores for each matching category.
 */
class TextClassificationEngine {

    data class ClassificationResult(
        val category: String,
        val confidence: Float,
        val matchedKeywords: List<String>
    )

    private val categoryKeywords = mapOf(
        "Prayer" to listOf(
            "صلاة", "صلوا", "أقيموا", "ركوع", "سجود", "وضوء", "قبلة",
            "prayer", "salah", "worship", "prostrate", "bow", "qibla"
        ),
        "Fasting" to listOf(
            "صيام", "رمضان", "صوم", "فطر", "سحور", "إفطار",
            "fast", "ramadan", "fasting", "iftar", "suhoor"
        ),
        "Charity" to listOf(
            "زكاة", "صدقة", "أنفقوا", "إنفاق", "عطاء", "فقير", "مسكين",
            "charity", "zakat", "spend", "poor", "needy", "alms"
        ),
        "Pilgrimage" to listOf(
            "حج", "عمرة", "كعبة", "مكة", "طواف", "سعي", "إحرام",
            "pilgrimage", "hajj", "umrah", "mecca", "kaaba", "tawaf"
        ),
        "Faith" to listOf(
            "إيمان", "آمنوا", "يؤمنون", "توحيد", "عقيدة", "يقين",
            "faith", "believe", "believers", "monotheism", "certainty"
        ),
        "Patience" to listOf(
            "صبر", "اصبروا", "صابرين", "تحمل", "ثبات",
            "patience", "patient", "persevere", "steadfast", "endure"
        ),
        "Forgiveness" to listOf(
            "مغفرة", "اغفر", "تاب", "توبة", "عفو", "رحمة",
            "forgiveness", "repent", "mercy", "pardon", "forgive"
        ),
        "Justice" to listOf(
            "عدل", "قسط", "ظلم", "إنصاف", "حق", "باطل",
            "justice", "fair", "oppression", "equity", "right", "wrong"
        ),
        "Creation" to listOf(
            "خلق", "السماوات", "الأرض", "كون", "وجود", "بداية",
            "creation", "heavens", "earth", "universe", "existence"
        ),
        "Afterlife" to listOf(
            "آخرة", "جنة", "نار", "يوم القيامة", "حساب", "ميزان", "صراط",
            "afterlife", "paradise", "hellfire", "judgment", "resurrection", "hereafter"
        ),
        "Prophets" to listOf(
            "نبي", "رسول", "موسى", "عيسى", "إبراهيم", "محمد", "آدم",
            "prophet", "messenger", "moses", "jesus", "abraham", "muhammad", "adam"
        ),
        "Gratitude" to listOf(
            "شكر", "اشكروا", "نعمة", "حمد", "الحمد",
            "gratitude", "thankful", "blessings", "praise", "thankfulness"
        )
    )

    /**
     * Classify text and return all matching categories with confidence scores.
     */
    fun classify(text: String): List<ClassificationResult> {
        val lowerText = text.lowercase()
        val results = mutableListOf<ClassificationResult>()

        categoryKeywords.forEach { (category, keywords) ->
            val matched = keywords.filter { keyword ->
                lowerText.contains(keyword.lowercase())
            }
            if (matched.isNotEmpty()) {
                val confidence = (matched.size.toFloat() / keywords.size).coerceIn(0f, 1f)
                results.add(ClassificationResult(category, confidence, matched))
            }
        }

        return results.sortedByDescending { it.confidence }
    }

    /**
     * Get the primary category for a text.
     */
    fun getPrimaryCategory(text: String): String? {
        return classify(text).firstOrNull()?.category
    }

    /**
     * Check if text belongs to a specific category with minimum confidence.
     */
    fun isCategory(text: String, category: String, minConfidence: Float = 0.1f): Boolean {
        return classify(text).any { it.category == category && it.confidence >= minConfidence }
    }
}
