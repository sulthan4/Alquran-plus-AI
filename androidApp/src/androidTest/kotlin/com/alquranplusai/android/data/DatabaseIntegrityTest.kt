package com.alquranplusai.android.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.alquranplusai.AlQuranDatabase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class DatabaseIntegrityTest {

    private lateinit var database: AlQuranDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        
        // We need to manually copy the asset to the database path for this test context
        // because we are testing the asset-based pre-population logic which might happen in App init usually
        val dbName = "alquran.db"
        val dbPath = context.getDatabasePath(dbName)
        
        if (!dbPath.exists()) {
            dbPath.parentFile?.mkdirs()
            context.assets.open("databases/$dbName").use { input ->
                FileOutputStream(dbPath).use { output ->
                    input.copyTo(output)
                }
            }
        }

        val driver = AndroidSqliteDriver(AlQuranDatabase.Schema, context, dbName)
        database = AlQuranDatabase(driver)
    }

    @Test
    fun verifySurahCount() {
        val count = database.surahQueries.count().executeAsOne()
        assertEquals("Should have 114 Surahs", 114, count)
    }

    @Test
    fun verifyAyahCount() {
        val count = database.ayahQueries.selectAll().executeAsList().size
        assertEquals("Should have 6236 Ayahs", 6236, count)
    }

    @Test
    fun verifyAlFatiha() {
        val fatiha = database.surahQueries.selectByNumber(1).executeAsOne()
        assertEquals("Al-Fatiha", fatiha.name)
        assertEquals(7, fatiha.numberOfAyahs)
        assertEquals("Meccan", fatiha.revelationType)
    }

    @Test
    fun verifyTranslation() {
        val count = database.translationQueries.selectDownloadedTranslations().executeAsList().size
        assertTrue("Should have at least 1 downloaded translation", count >= 1)
        
        val translation = database.translationQueries.selectDownloadedTranslations().executeAsList().first()
        assertEquals("en.sahih", translation.id)
    }
}
