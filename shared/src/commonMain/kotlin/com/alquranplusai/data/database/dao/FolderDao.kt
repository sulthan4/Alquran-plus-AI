package com.alquranplusai.data.database.dao

import com.alquranplusai.data.database.AlQuranDatabase

class FolderDao(private val database: AlQuranDatabase) {
    val queries get() = database.folderQueries
}
