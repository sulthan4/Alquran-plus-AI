package com.alquranplusai.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a quiz
 */
@Serializable
data class Quiz(
    val id: String,
    val title: String,
    val description: String? = null,
    val category: QuizCategory,
    val difficulty: QuizDifficulty,
    val questionCount: Int,
    val timeLimit: Int? = null,
    val passingScore: Int = 70,
    val tags: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long,
    val attemptCount: Int = 0,
    val averageScore: Float = 0f,
    val isPublished: Boolean = true
)

/**
 * Represents a quiz question
 */
@Serializable
data class Question(
    val id: String,
    val quizId: String,
    val type: QuestionType,
    val question: String,
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String? = null,
    val hint: String? = null,
    val points: Int = 1,
    val timeLimit: Int? = null,
    val difficulty: QuizDifficulty = QuizDifficulty.MEDIUM,
    val position: Int
)

/**
 * Quiz attempt/session
 */
@Serializable
data class QuizAttempt(
    val id: String,
    val quizId: String,
    val userId: String,
    val startedAt: Long,
    val completedAt: Long? = null,
    val score: Int = 0,
    val totalPoints: Int,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val skippedAnswers: Int = 0,
    val timeSpent: Long = 0,
    val isPassed: Boolean = false,
    val answers: Map<String, String> = emptyMap()
)

/**
 * Daily challenge
 */
@Serializable
data class DailyChallenge(
    val id: String,
    val date: String,
    val quizId: String,
    val title: String,
    val description: String,
    val reward: Int = 10,
    val participantCount: Int = 0,
    val completionCount: Int = 0
)

/**
 * Quiz session (in-progress)
 */
@Serializable
data class QuizSession(
    val id: String,
    val quizId: String,
    val currentQuestionIndex: Int = 0,
    val answers: MutableMap<String, String> = mutableMapOf(),
    val startTime: Long,
    val timeRemaining: Long? = null,
    val isPaused: Boolean = false
)

/**
 * Quiz result summary
 */
@Serializable
data class QuizResult(
    val attemptId: String,
    val quizId: String,
    val score: Int,
    val percentage: Float,
    val correctCount: Int,
    val wrongCount: Int,
    val skippedCount: Int,
    val totalQuestions: Int,
    val timeSpent: Long,
    val isPassed: Boolean,
    val rank: Int? = null,
    val questionResults: List<QuestionResult> = emptyList()
)

/**
 * Individual question result
 */
@Serializable
data class QuestionResult(
    val questionId: String,
    val userAnswer: String?,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val points: Int,
    val timeSpent: Long
)

/**
 * Quiz statistics
 */
@Serializable
data class QuizStatistics(
    val userId: String,
    val totalAttempts: Int = 0,
    val totalQuizzes: Int = 0,
    val averageScore: Float = 0f,
    val bestScore: Int = 0,
    val totalTimeSpent: Long = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastAttemptDate: Long? = null,
    val categoryStats: Map<QuizCategory, CategoryStats> = emptyMap()
)

/**
 * Statistics per category
 */
@Serializable
data class CategoryStats(
    val category: QuizCategory,
    val attempts: Int = 0,
    val averageScore: Float = 0f,
    val bestScore: Int = 0
)

/**
 * Leaderboard entry
 */
@Serializable
data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val score: Int,
    val avatarUrl: String? = null,
    val country: String? = null,
    val isCurrentUser: Boolean = false
)

/**
 * Achievement
 */
@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val category: AchievementCategory,
    val requirement: Int,
    val reward: Int = 0,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val progress: Int = 0
)

/**
 * Goal
 */
@Serializable
data class Goal(
    val id: String,
    val type: GoalType,
    val title: String,
    val description: String? = null,
    val target: Int,
    val current: Int = 0,
    val startDate: Long,
    val endDate: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val reminder: Boolean = false,
    val reminderTime: String? = null
)

/**
 * Quiz category
 */
@Serializable
enum class QuizCategory {
    GENERAL_KNOWLEDGE,
    SURAH_NAMES,
    AYAH_COMPLETION,
    PROPHETS,
    STORIES,
    RULES_FIQH,
    HISTORY,
    MEMORIZATION,
    TAJWEED,
    TRANSLATION,
    TAFSIR,
    CUSTOM
}

/**
 * Quiz difficulty
 */
@Serializable
enum class QuizDifficulty {
    BEGINNER,
    EASY,
    MEDIUM,
    HARD,
    EXPERT,
    MASTER
}

/**
 * Question type
 */
@Serializable
enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    FILL_IN_BLANK,
    MATCHING,
    ORDERING
}

/**
 * Achievement category
 */
@Serializable
enum class AchievementCategory {
    READING,
    QUIZ,
    MEMORIZATION,
    STREAK,
    SOCIAL,
    SPECIAL
}

/**
 * Goal type
 */
@Serializable
enum class GoalType {
    DAILY_READING,
    WEEKLY_READING,
    MONTHLY_READING,
    SURAH_COMPLETION,
    JUZ_COMPLETION,
    QUIZ_COMPLETION,
    STREAK,
    CUSTOM
}
