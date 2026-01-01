package com.alquranplusai.data.database

import com.alquranplusai.data.database.AlQuranDatabase

interface AlQuranDatabaseWrapper {
    val surahQueries: com.alquranplusai.database.SurahQueries
    val ayahQueries: com.alquranplusai.database.AyahQueries
    val wordQueries: com.alquranplusai.database.WordQueries
    val translationQueries: com.alquranplusai.database.TranslationQueries
    val audioQueries: com.alquranplusai.database.AudioQueries
    val playlistQueries: com.alquranplusai.database.PlaylistQueries
    val playlistItemQueries: com.alquranplusai.database.PlaylistItemQueries
    val bookmarkQueries: com.alquranplusai.database.BookmarkQueries
    val folderQueries: com.alquranplusai.database.FolderQueries
    val bookmarkTagQueries: com.alquranplusai.database.BookmarkTagQueries
    val noteQueries: com.alquranplusai.database.NoteQueries
    val quizQueries: com.alquranplusai.database.QuizQueries
    val questionQueries: com.alquranplusai.database.QuestionQueries
    val quizSessionQueries: com.alquranplusai.database.QuizSessionQueries
    val quizResultQueries: com.alquranplusai.database.QuizResultQueries
    val userQueries: com.alquranplusai.database.UserQueries
    val settingsQueries: com.alquranplusai.database.SettingsQueries
    val achievementQueries: com.alquranplusai.database.AchievementQueries
    val goalQueries: com.alquranplusai.database.GoalQueries
    val analyticsQueries: com.alquranplusai.database.AnalyticsQueries
    val searchQueries: com.alquranplusai.database.SearchQueries
    val tafsirQueries: com.alquranplusai.database.TafsirQueries
    fun transaction(noEnclosing: Boolean = false, body: () -> Unit)
    fun close()
}

class RealAlQuranDatabaseWrapper(
    driverFactory: DatabaseDriverFactory
) : AlQuranDatabaseWrapper {
    private val driver = driverFactory.createDriver()
    val database = AlQuranDatabase(driver)
    
    // Member properties to access query interfaces
    override val surahQueries get() = database.surahQueries
    override val ayahQueries get() = database.ayahQueries
    override val wordQueries get() = database.wordQueries
    override val translationQueries get() = database.translationQueries
    override val audioQueries get() = database.audioQueries
    override val playlistQueries get() = database.playlistQueries
    override val playlistItemQueries get() = database.playlistItemQueries
    override val bookmarkQueries get() = database.bookmarkQueries
    override val folderQueries get() = database.folderQueries
    override val bookmarkTagQueries get() = database.bookmarkTagQueries
    override val noteQueries get() = database.noteQueries
    override val quizQueries get() = database.quizQueries
    override val questionQueries get() = database.questionQueries
    override val quizSessionQueries get() = database.quizSessionQueries
    override val quizResultQueries get() = database.quizResultQueries
    override val userQueries get() = database.userQueries
    override val settingsQueries get() = database.settingsQueries
    override val achievementQueries get() = database.achievementQueries
    override val goalQueries get() = database.goalQueries
    override val analyticsQueries get() = database.analyticsQueries
    override val searchQueries get() = database.searchQueries
    override val tafsirQueries get() = database.tafsirQueries

    override fun transaction(noEnclosing: Boolean, body: () -> Unit) {
        database.transaction(noEnclosing) {
            body()
        }
    }

    override fun close() {
        driver.close()
    }
}
