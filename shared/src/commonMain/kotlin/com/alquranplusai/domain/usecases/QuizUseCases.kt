package com.alquranplusai.domain.usecases

import com.alquranplusai.domain.models.Quiz
import com.alquranplusai.domain.models.QuizResult
import com.alquranplusai.domain.repositories.QuizRepository
import kotlinx.coroutines.flow.Flow

class StartQuizUseCase(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(quizId: String): Result<Quiz?> {
        return try {
            quizRepository.startQuizSession(quizId)
            var quiz: Quiz? = null
            quizRepository.getQuizById(quizId).collect { quiz = it }
            Result.success(quiz)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SubmitQuizAnswersUseCase(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(
        quizId: String,
        answers: Map<String, String>,
        timeSpentMillis: Long
    ): Result<QuizSubmissionResult> {
        return try {
            // Calculate score
            // TODO: Implement proper question fetching and scoring
            val correctCount = 0
            val totalQuestions = answers.size
            
            val score = if (totalQuestions > 0) {
                (correctCount * 100) / totalQuestions
            } else 0
            
            // Submit result
            quizRepository.submitQuizResult(quizId, score, totalQuestions)
            
            Result.success(
                QuizSubmissionResult(
                    score = score,
                    correctAnswers = correctCount,
                    totalQuestions = totalQuestions,
                    timeSpent = timeSpentMillis,
                    passed = score >= 70
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetQuizHistoryUseCase(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(): List<QuizHistoryItem> {
        // TODO: Implement quiz history
        return emptyList()
    }
}

data class QuizSubmissionResult(
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeSpent: Long,
    val passed: Boolean
)

data class QuizHistoryItem(
    val quiz: Quiz,
    val isCompleted: Boolean,
    val lastAttemptDate: Long?,
    val bestScore: Int?
)
