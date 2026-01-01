package com.alquranplusai.data.database.dao
import com.alquranplusai.data.database.AlQuranDatabase
class BookmarkTagDao(private val database: AlQuranDatabase) { val queries get() = database.bookmarkTagQueries }
