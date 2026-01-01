package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.QuizDto
import com.alquranplusai.data.network.dto.QuestionDto
import com.alquranplusai.data.network.dto.QuizResultDto

/**
 * API service for quizzes
 */
interface QuizApiService {
    
    suspend fun getAllQuizzes(): List<QuizDto>
    
    suspend fun getQuiz(id: Int): QuizDto
    
    suspend fun getQuizzesByCategory(category: String): List<QuizDto>
    
    suspend fun getQuestions(quizId: Int): List<QuestionDto>
    
    suspend fun submitQuizResult(userId: Int, result: QuizResultDto): QuizResultDto
    
    suspend fun getUserResults(userId: Int): List<QuizResultDto>
}
