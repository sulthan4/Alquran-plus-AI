package com.alquranplusai.data.repositories

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.alquranplusai.data.database.AlQuranDatabase
import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.data.network.api.FakeSearchApiService
import com.alquranplusai.data.ai.FakeSemanticSearchEngine
import com.alquranplusai.data.network.dto.QuranFoundationSearchResultDto
import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchRepositoryImplTest {
    
    // Functional Fake for the Database Wrapper using in-memory driver
    private class TestAlQuranDatabaseWrapper : AlQuranDatabaseWrapper {
        private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        private val database: AlQuranDatabase

        init {
            AlQuranDatabase.Schema.create(driver)
            database = AlQuranDatabase(driver)
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

    private val api = FakeSearchApiService()
    private val semanticSearch = FakeSemanticSearchEngine()
    private lateinit var database: TestAlQuranDatabaseWrapper
    private lateinit var repository: SearchRepositoryImpl

    @BeforeTest
    fun setup() {
        database = TestAlQuranDatabaseWrapper()
        repository = SearchRepositoryImpl(database, api, semanticSearch)
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `searchText should return API results when available`() = runTest {
        // Arrange
        val query = "sabr"
        api.searchResults = listOf(
            QuranFoundationSearchResultDto(
                verseId = 1,
                verseKey = "2:153",
                text = "O you who have believed, seek help through patience and prayer.",
                highlighted = "O you who have believed, seek help through <em>patience</em> and prayer."
            )
        )

        // Act
        val results = repository.searchText(query, SearchOptions()).first()

        // Assert
        assertEquals(1, results.size)
        assertEquals("1", results[0].id)
        assertEquals(2, results[0].surahNumber)
        assertEquals(153, results[0].ayahNumber)
        assertEquals(MatchType.EXACT, results[0].matchType)
        assertEquals(query, api.lastQuery)
    }

    @Test
    fun `searchText should fallback to semantic search when API fails`() = runTest {
        // Arrange
        val query = "concept of patience"
        api.shouldThrowException = true
        
        semanticSearch.semanticResults = listOf(
            SearchResult(
                id = "semantic_1",
                surahNumber = 2,
                ayahNumber = 153,
                text = "Patience is a virtue",
                relevanceScore = 0.95f,
                matchType = MatchType.SEMANTIC
            )
        )

        // Act
        val results = repository.searchText(query, SearchOptions()).first()

        // Assert
        assertEquals(1, results.size)
        assertEquals(MatchType.SEMANTIC, results[0].matchType)
        assertEquals(query, api.lastQuery)
        assertEquals(query, semanticSearch.lastQuery)
    }
}
