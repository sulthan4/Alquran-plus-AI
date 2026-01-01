package com.alquranplusai.data.database

/**
 * Database migrations for schema versioning
 */
object DatabaseMigrations {
    
    const val CURRENT_VERSION = 1
    
    fun getMigrations(): List<Migration> {
        return listOf(
            // Future migrations will be added here
            // Migration(1, 2) { database -> ... }
        )
    }
}

data class Migration(
    val fromVersion: Int,
    val toVersion: Int,
    val migrate: (Any) -> Unit
)
