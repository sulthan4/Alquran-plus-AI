package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    val id: Int,
    val title: String,
    val description: String? = null,
    val category: String,
    val difficulty: String,
    val timeLimit: Int? = null,
    val questionCount: Int,
    val createdAt: Long
)

@Serializable
data class QuestionDto(
    val id: Int,
    val quizId: Int,
    val questionText: String,
    val questionType: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String? = null,
    val points: Int = 1
)

@Serializable
data class QuizResultDto(
    val id: Int,
    val userId: Int,
    val quizId: Int,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeSpent: Long,
    val completedAt: Long
)
