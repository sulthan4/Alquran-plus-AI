package com.alquranplusai.data.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.alquranplusai.data.database.AlQuranDatabase
import com.alquranplusai.data.database.AlQuranDatabaseWrapper

class TestAlQuranDatabaseWrapper : AlQuranDatabaseWrapper {
    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    private val database: AlQuranDatabase

    init {
        AlQuranDatabase.Schema.create(driver)
        database = AlQuranDatabase(driver)
        // Enable foreign keys for SQLite
        driver.execute(null, "PRAGMA foreign_keys=ON", 0)
    }

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
