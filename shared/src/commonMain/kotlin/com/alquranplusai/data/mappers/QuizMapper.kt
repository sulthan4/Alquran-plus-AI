package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*

/** Mapper for Quiz data */
class QuizMapper {

    fun mapQuizDtoToDomain(dto: QuizDto): Quiz {
        return Quiz(
                id = dto.id.toString(),
                title = dto.title,
                category =
                        when (dto.category.uppercase()) {
                            "QURAN" -> QuizCategory.GENERAL_KNOWLEDGE
                            "TAFSIR" -> QuizCategory.TAFSIR
                            "TAJWEED" -> QuizCategory.TAJWEED
                            "HISTORY" -> QuizCategory.HISTORY
                            else -> QuizCategory.CUSTOM
                        },
                difficulty =
                        when (dto.difficulty?.uppercase()) {
                            "EASY" -> QuizDifficulty.EASY
                            "MEDIUM" -> QuizDifficulty.MEDIUM
                            "HARD" -> QuizDifficulty.HARD
                            else -> QuizDifficulty.MEDIUM
                        },
                questionCount = 10, // Default value
                createdAt = dto.createdAt,
                updatedAt = dto.createdAt
        )
    }

    fun mapQuestionDtoToDomain(dto: QuestionDto): Question {
        return Question(
                id = dto.id.toString(),
                quizId = dto.quizId.toString(),
                type = QuestionType.MULTIPLE_CHOICE,
                question = dto.questionText,
                options = dto.options,
                correctAnswer = dto.correctAnswer,
                position = 0
        )
    }

    fun mapQuizResultDtoToDomain(dto: QuizResultDto): QuizResult {
        val percentage =
                if (dto.totalQuestions > 0) {
                    (dto.score.toFloat() / dto.totalQuestions.toFloat()) * 100
                } else 0f

        return QuizResult(
                attemptId = dto.id.toString(),
                quizId = dto.quizId.toString(),
                score = dto.score,
                percentage = percentage,
                correctCount = dto.score,
                wrongCount = dto.totalQuestions - dto.score,
                skippedCount = 0,
                totalQuestions = dto.totalQuestions,
                timeSpent = 0L,
                isPassed = percentage >= 70
        )
    }
}
