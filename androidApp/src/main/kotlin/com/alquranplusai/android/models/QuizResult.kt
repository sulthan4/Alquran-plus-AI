package com.alquranplusai.android.models

/**
 * Quiz result data
 */
data class QuizResult(
    val quizId: Long,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeSpent: Long
)
