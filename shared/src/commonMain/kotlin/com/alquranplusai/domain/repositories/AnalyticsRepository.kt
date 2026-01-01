package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/** Repository for analytics and progress tracking */
interface AnalyticsRepository {
    // Reading session tracking
    suspend fun startReadingSession(
            userId: String,
            surahNumber: Int,
            ayahNumber: Int,
            mode: ReadingMode
    ): Flow<String>
    suspend fun endReadingSession(sessionId: String, endAyah: Int)
    suspend fun updateReadingSession(sessionId: String, interaction: SessionInteraction)
    fun getReadingSession(sessionId: String): Flow<ReadingSession?>
    fun getUserReadingSessions(userId: String, limit: Int = 100): Flow<List<ReadingSession>>
    fun getSessionsByDateRange(
            userId: String,
            startDate: String,
            endDate: String
    ): Flow<List<ReadingSession>>

    // Reading patterns
    fun getReadingPattern(userId: String): Flow<ReadingPattern>
    fun analyzeReadingBehavior(userId: String): Flow<ReadingPattern>

    // Reports
    fun getWeeklyReport(userId: String, weekStart: String): Flow<WeeklyReport>
    fun getMonthlyInsight(userId: String, month: String, year: Int): Flow<MonthlyInsight>
    fun getYearlyOverview(userId: String, year: Int): Flow<YearlyOverview>
    fun getCurrentWeekReport(userId: String): Flow<WeeklyReport>
    fun getCurrentMonthInsight(userId: String): Flow<MonthlyInsight>

    // Statistics
    fun getUserStatistics(userId: String): Flow<UserStatisticsSummary>
    suspend fun updateUserStatistics(userId: String)
    fun getTotalReadingTime(userId: String): Flow<Long>
    fun getTotalAyahsRead(userId: String): Flow<Int>
    fun getUserAchievements(userId: String): Flow<List<Achievement>>
    suspend fun unlockAchievement(userId: String, achievementId: String)
    fun getUserGoals(userId: String): Flow<List<Goal>>
    suspend fun createGoal(userId: String, goal: Goal)
    suspend fun updateGoalProgress(userId: String, goalId: String, progress: Int)
    suspend fun deleteGoal(goalId: String)
    fun getCurrentStreak(userId: String): Flow<Int>
    fun getLongestStreak(userId: String): Flow<Int>
    fun getQuranCompletionCount(userId: String): Flow<Int>

    // Milestones
    fun getUserMilestones(userId: String): Flow<List<Milestone>>
    suspend fun addMilestone(userId: String, milestone: Milestone)
    suspend fun checkAndAwardMilestones(userId: String)

    // Charts and visualizations
    fun getReadingTimeChart(userId: String, range: AnalyticsTimeRange): Flow<List<ChartDataPoint>>
    fun getAyahsReadChart(userId: String, range: AnalyticsTimeRange): Flow<List<ChartDataPoint>>
    fun getSurahCompletionChart(userId: String): Flow<List<ChartDataPoint>>
    fun getStreakChart(userId: String, days: Int = 30): Flow<List<ChartDataPoint>>

    // Comparisons
    fun compareWithPreviousPeriod(userId: String, range: AnalyticsTimeRange): Flow<ComparisonData>
    fun compareWithAverage(userId: String): Flow<ComparisonData>

    // Sync
    fun syncAnalytics(userId: String): Flow<SyncStatus>
    fun getLastSyncTime(userId: String): Flow<Long?>
}
