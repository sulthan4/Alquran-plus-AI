package com.alquranplusai.data.database

import app.cash.sqldelight.db.SqlDriver

/**
 * Factory for creating platform-specific database drivers
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
