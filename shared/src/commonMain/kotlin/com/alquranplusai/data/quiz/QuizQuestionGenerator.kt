package com.alquranplusai.data.quiz

import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Quiz question generator that creates questions from Quran content
 */
class QuizQuestionGenerator(
    private val quranRepository: QuranRepository
) {
    
    /**
     * Generate quiz questions based on category and difficulty
     */
    suspend fun generateQuestions(
        category: QuizCategory,
        difficulty: QuizDifficulty,
        count: Int
    ): List<Question> {
        return when (category) {
            QuizCategory.MEMORIZATION -> generateMemorizationQuestions(difficulty, count)
            QuizCategory.TRANSLATION -> generateTranslationQuestions(difficulty, count)
            QuizCategory.TAFSIR -> generateTafsirQuestions(difficulty, count)
            QuizCategory.GENERAL_KNOWLEDGE -> generateGeneralQuestions(difficulty, count)
            QuizCategory.SURAH_NAMES, QuizCategory.AYAH_COMPLETION -> generateSurahInfoQuestions(difficulty, count)
            else -> generateGeneralQuestions(difficulty, count) // Default for other categories
        }
    }
    
    /**
     * Generate memorization questions (complete the verse, identify surah, etc.)
     */
    private suspend fun generateMemorizationQuestions(
        difficulty: QuizDifficulty,
        count: Int
    ): List<Question> {
        val questions = mutableListOf<Question>()
        val allSurahs = quranRepository.getAllSurahs().first()
        
        repeat(count) {
            // Pick random surah
            val surah = allSurahs.random()
            val ayahs = quranRepository.getAyahsBySurah(surah.number).first()
            
            if (ayahs.isEmpty()) return@repeat
            
            // Pick random ayah
            val ayah = ayahs.random()
            
            when (Random.nextInt(3)) {
                0 -> {
                    // Complete the verse question
                    val words = ayah.textUthmani.split(" ")
                    if (words.size < 4) return@repeat
                    
                    val cutPoint = words.size / 2
                    val firstHalf = words.take(cutPoint).joinToString(" ")
                    val secondHalf = words.drop(cutPoint).joinToString(" ")
                    
                    // Generate wrong answers from other ayahs
                    val wrongAnswers = ayahs.filter { it.id != ayah.id }
                        .shuffled()
                        .take(3)
                        .map { it.textUthmani.split(" ").drop(it.textUthmani.split(" ").size / 2).joinToString(" ") }
                    
                    questions.add(
                        Question(
                            id = "mem_${ayah.id}_complete",
                            quizId = "",
                            question = "Complete the verse: $firstHalf ...",
                            type = QuestionType.MULTIPLE_CHOICE,
                            options = (wrongAnswers + secondHalf).shuffled(),
                            correctAnswer = secondHalf,
                            explanation = "Full verse: ${ayah.textUthmani}",
                            points = getPointsForDifficulty(difficulty),
                            timeLimit = 30,
                            surahNumber = surah.number,
                            ayahNumber = ayah.ayahNumber,
                            position = questions.size
                        )
                    )
                }
                1 -> {
                    // Identify surah question
                    val wrongSurahs = allSurahs.filter { it.number != surah.number }
                        .shuffled()
                        .take(3)
                        .map { it.name }
                    
                    questions.add(
                        Question(
                            id = "mem_${ayah.id}_surah",
                            quizId = "",
                            question = "Which surah contains this verse?\n\n${ayah.textUthmani}",
                            type = QuestionType.MULTIPLE_CHOICE,
                            options = (wrongSurahs + surah.name).shuffled(),
                            correctAnswer = surah.name,
                            explanation = "This is from Surah ${surah.name} (${surah.number}:${ayah.ayahNumber})",
                            points = getPointsForDifficulty(difficulty),
                            timeLimit = 30,
                            surahNumber = surah.number,
                            ayahNumber = ayah.ayahNumber,
                            position = questions.size
                        )
                    )
                }
                2 -> {
                    // Verse number question
                    val wrongNumbers = (1..surah.numberOfAyahs).filter { it != ayah.ayahNumber }
                        .shuffled()
                        .take(3)
                        .map { it.toString() }
                    
                    questions.add(
                        Question(
                            id = "mem_${ayah.id}_number",
                            quizId = "",
                            question = "What is the verse number of this ayah in Surah ${surah.name}?\n\n${ayah.textUthmani}",
                            type = QuestionType.MULTIPLE_CHOICE,
                            options = (wrongNumbers + ayah.ayahNumber.toString()).shuffled(),
                            correctAnswer = ayah.ayahNumber.toString(),
                            explanation = "This is verse ${ayah.ayahNumber} of Surah ${surah.name}",
                            points = getPointsForDifficulty(difficulty),
                            timeLimit = 20,
                            surahNumber = surah.number,
                            ayahNumber = ayah.ayahNumber,
                            position = questions.size
                        )
                    )
                }
            }
        }
        
        return questions
    }
    
    /**
     * Generate translation questions
     */
    private suspend fun generateTranslationQuestions(
        difficulty: QuizDifficulty,
        count: Int
    ): List<Question> {
        val questions = mutableListOf<Question>()
        val allSurahs = quranRepository.getAllSurahs().first()
        
        repeat(count) {
            val surah = allSurahs.random()
            val ayahs = quranRepository.getAyahsBySurah(surah.number).first()
            
            if (ayahs.isEmpty()) return@repeat
            
            val ayah = ayahs.filter { it.translations.isNotEmpty() }.randomOrNull() ?: return@repeat
            val translation = ayah.translations.firstOrNull() ?: return@repeat
            
            // Match Arabic to translation
            val wrongTranslations = ayahs
                .filter { it.id != ayah.id && it.translations.isNotEmpty() }
                .shuffled()
                .take(3)
                .mapNotNull { it.translations.firstOrNull()?.text }
            
            questions.add(
                Question(
                    id = "trans_${ayah.id}",
                    quizId = "",
                    question = "What is the translation of this verse?\n\n${ayah.textUthmani}",
                    type = QuestionType.MULTIPLE_CHOICE,
                    options = (wrongTranslations + translation.text).shuffled(),
                    correctAnswer = translation.text,
                    explanation = "Translation: ${translation.text}",
                    points = getPointsForDifficulty(difficulty),
                    timeLimit = 45,
                    surahNumber = surah.number,
                    ayahNumber = ayah.ayahNumber,
                    position = questions.size
                )
            )
        }
        
        return questions
    }
    
    /**
     * Generate tafsir/interpretation questions
     */
    private suspend fun generateTafsirQuestions(
        difficulty: QuizDifficulty,
        count: Int
    ): List<Question> {
        // Placeholder - would need tafsir data
        return generateGeneralQuestions(difficulty, count)
    }
    
    /**
     * Generate general knowledge questions about Quran
     */
    private suspend fun generateGeneralQuestions(
        difficulty: QuizDifficulty,
        count: Int
    ): List<Question> {
        val questions = mutableListOf<Question>()
        
        val generalQuestions = listOf(
            Triple("How many surahs are in the Quran?", "114", listOf("110", "112", "116")),
            Triple("How many juz (parts) are in the Quran?", "30", listOf("25", "28", "32")),
            Triple("What is the longest surah in the Quran?", "Al-Baqarah", listOf("Al-Imran", "An-Nisa", "Al-Maidah")),
            Triple("What is the shortest surah in the Quran?", "Al-Kawthar", listOf("Al-Ikhlas", "Al-Falaq", "An-Nas")),
            Triple("In which city was the Quran first revealed?", "Makkah", listOf("Madinah", "Jerusalem", "Damascus"))
        )
        
        repeat(minOf(count, generalQuestions.size)) { index ->
            val (questionText, correctAnswer, wrongAnswers) = generalQuestions[index]
            
            questions.add(
                Question(
                    id = "general_$index",
                    quizId = "",
                    question = questionText,
                    type = QuestionType.MULTIPLE_CHOICE,
                    options = (wrongAnswers + correctAnswer).shuffled(),
                    correctAnswer = correctAnswer,
                    explanation = "The correct answer is: $correctAnswer",
                    points = getPointsForDifficulty(difficulty),
                    timeLimit = 30,
                    position = questions.size
                )
            )
        }
        
        return questions
    }
    
    /**
     * Generate surah information questions
     */
    private suspend fun generateSurahInfoQuestions(
        difficulty: QuizDifficulty,
        count: Int
    ): List<Question> {
        val questions = mutableListOf<Question>()
        val allSurahs = quranRepository.getAllSurahs().first()
        
        repeat(count) {
            val surah = allSurahs.random()
            
            when (Random.nextInt(3)) {
                0 -> {
                    // Ayah count question
                    val wrongCounts = allSurahs.filter { it.number != surah.number }
                        .shuffled()
                        .take(3)
                        .map { it.numberOfAyahs.toString() }
                    
                    questions.add(
                        Question(
                            id = "surah_${surah.number}_count",
                            quizId = "",
                            question = "How many verses are in Surah ${surah.name}?",
                            type = QuestionType.MULTIPLE_CHOICE,
                            options = (wrongCounts + surah.numberOfAyahs.toString()).shuffled(),
                            correctAnswer = surah.numberOfAyahs.toString(),
                            explanation = "Surah ${surah.name} has ${surah.numberOfAyahs} verses",
                            points = getPointsForDifficulty(difficulty),
                            timeLimit = 20,
                            position = questions.size
                        )
                    )
                }
                1 -> {
                    // Revelation place question
                    val revelationPlace = if (surah.revelationType == RevelationType.MECCAN) "Makkah" else "Madinah"
                    
                    questions.add(
                        Question(
                            id = "surah_${surah.number}_place",
                            quizId = "",
                            question = "Where was Surah ${surah.name} revealed?",
                            type = QuestionType.MULTIPLE_CHOICE,
                            options = listOf("Makkah", "Madinah").shuffled(),
                            correctAnswer = revelationPlace,
                            explanation = "Surah ${surah.name} was revealed in $revelationPlace",
                            points = getPointsForDifficulty(difficulty),
                            timeLimit = 20,
                            position = questions.size
                        )
                    )
                }
                2 -> {
                    // Surah number question
                    val wrongNumbers = (1..114).filter { it != surah.number }
                        .shuffled()
                        .take(3)
                        .map { it.toString() }
                    
                    questions.add(
                        Question(
                            id = "surah_${surah.number}_number",
                            quizId = "",
                            question = "What is the number of Surah ${surah.name}?",
                            type = QuestionType.MULTIPLE_CHOICE,
                            options = (wrongNumbers + surah.number.toString()).shuffled(),
                            correctAnswer = surah.number.toString(),
                            explanation = "Surah ${surah.name} is number ${surah.number} in the Quran",
                            points = getPointsForDifficulty(difficulty),
                            timeLimit = 20,
                            position = questions.size
                        )
                    )
                }
            }
        }
        
        return questions
    }
    
    private fun getPointsForDifficulty(difficulty: QuizDifficulty): Int {
        return when (difficulty) {
            QuizDifficulty.BEGINNER, QuizDifficulty.EASY -> 5
            QuizDifficulty.MEDIUM -> 10
            QuizDifficulty.HARD, QuizDifficulty.EXPERT, QuizDifficulty.MASTER -> 15
        }
    }
}
