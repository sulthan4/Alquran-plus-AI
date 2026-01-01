package com.alquranplusai.domain.models

// Analytics Statistics Models
data class DailyStatistics(
    val date: Long,
    val readingTime: Int,
    val versesRead: Int,
    val surahsCompleted: Int,
    val audioListened: Int,
    val quizzesTaken: Int,
    val streak: Int
)

data class WeeklyReport(
    val weekStart: Long,
    val weekEnd: Long,
    val totalReadingTime: Int,
    val totalVersesRead: Int,
    val dailyStats: List<DailyStatistics>,
    val topSurahs: List<Pair<Int, Int>>,
    val streak: Int,
    val goalsAchieved: Int
)

data class MonthlyInsight(
    val month: Long,
    val totalReadingTime: Int,
    val totalVersesRead: Int,
    val surahsCompleted: Int,
    val averageDailyTime: Int,
    val longestStreak: Int,
    val currentStreak: Int,
    val topCategories: List<String>,
    val milestones: List<String>
)

data class YearlyOverview(
    val year: Int,
    val totalReadingTime: Int,
    val totalVersesRead: Int,
    val surahsCompleted: Int,
    val quizzesTaken: Int,
    val averageScore: Double,
    val monthlyData: List<MonthlyInsight>,
    val topAchievements: List<String>,
    val yearProgress: Double
)

data class AllTimeStatistics(
    val totalReadingTime: Int,
    val totalVersesRead: Int,
    val totalSurahsCompleted: Int,
    val totalQuizzesTaken: Int,
    val totalAchievements: Int,
    val accountAge: Int,
    val favoriteReciter: String?,
    val mostReadSurah: String?
)

data class ReadingPattern(
    val preferredTime: List<Int>,
    val averageSessionDuration: Int,
    val mostReadSurahs: List<Int>,
    val readingFrequency: Map<String, Int>,
    val consistency: Double
)

data class ChartData(
    val label: String,
    val value: Float,
    val timestamp: Long? = null
)

data class ComparisonData(
    val current: Int,
    val previous: Int,
    val percentageChange: Double,
    val trend: Trend
)

enum class Trend {
    UP, DOWN, STABLE
}

enum class TimePeriod {
    DAY, WEEK, MONTH, YEAR, ALL_TIME
}

// Additional Analytics Models
data class SessionInteraction(
    val type: InteractionType,
    val surahNumber: Int,
    val ayahNumber: Int,
    val timestamp: Long,
    val duration: Int? = null
)

enum class InteractionType {
    READ, LISTEN, BOOKMARK, NOTE, SHARE
}

data class ReadingSession(
    val id: String,
    val userId: String,
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int?,
    val mode: ReadingMode,
    val startTime: Long,
    val endTime: Long?,
    val duration: Int,
    val interactions: List<SessionInteraction> = emptyList()
)

data class UserStatisticsSummary(
    val userId: String,
    val totalReadingTime: Int,
    val totalVersesRead: Int,
    val totalSurahsCompleted: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val quranCompletions: Int,
    val totalQuizzesTaken: Int,
    val averageQuizScore: Double
)

data class Milestone(
    val id: Long,
    val type: MilestoneType,
    val title: String,
    val description: String,
    val value: Int,
    val achievedAt: Long,
    val isShared: Boolean = false
)

enum class MilestoneType {
    READING, MEMORIZATION, QUIZ, STREAK, COMPLETION
}

data class ChartDataPoint(
    val label: String,
    val value: Float,
    val timestamp: Long
)

enum class AnalyticsTimeRange {
    DAY, WEEK, MONTH, YEAR, ALL_TIME
}

enum class SyncStatus {
    IDLE, SYNCING, SUCCESS, ERROR
}

