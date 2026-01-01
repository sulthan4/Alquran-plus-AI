package com.alquranplusai.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.alquranplusai.AlQuranDatabase

/** iOS implementation of DatabaseDriverFactory */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AlQuranDatabase.Schema, "alquran.db")
    }
}
