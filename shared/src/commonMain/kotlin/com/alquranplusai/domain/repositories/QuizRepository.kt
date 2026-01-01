package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for quiz operations
 */
interface QuizRepository {
    // Quiz operations
    suspend fun getAllQuizzes(): Flow<List<Quiz>>
    suspend fun getQuizById(id: String): Flow<Quiz?>
    suspend fun getQuizzesByCategory(category: QuizCategory): Flow<List<Quiz>>
    suspend fun getQuizzesByDifficulty(difficulty: QuizDifficulty): Flow<List<Quiz>>
    suspend fun generateQuiz(category: QuizCategory, difficulty: QuizDifficulty, questionCount: Int): Flow<Quiz>
    suspend fun getDailyChallenge(): Flow<DailyChallenge?>
    suspend fun getCompletedQuizzes(): Flow<List<String>>
    
    // Question operations
    suspend fun getQuizQuestions(quizId: String): Flow<List<Question>>
    suspend fun getQuestionsByQuiz(quizId: String): Flow<List<Question>> // Alias for compatibility
    
    // Session operations
    suspend fun startQuizSession(quizId: String): Flow<String>
    suspend fun submitAnswer(sessionId: String, questionId: String, answerId: String)
    suspend fun endQuizSession(sessionId: String): Flow<QuizResult>
    suspend fun submitQuizResult(quizId: String, score: Int, totalQuestions: Int)
    
    // Results operations
    suspend fun getQuizHistory(): Flow<List<QuizResult>>
    suspend fun getQuizResults(): Flow<List<QuizResult>> // Alias for compatibility
    suspend fun getQuizResultById(id: String): Flow<QuizResult?>
    suspend fun deleteQuizResult(id: String)
}
