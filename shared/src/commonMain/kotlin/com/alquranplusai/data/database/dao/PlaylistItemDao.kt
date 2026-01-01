package com.alquranplusai.data.database.dao

import com.alquranplusai.data.database.AlQuranDatabase

class PlaylistItemDao(private val database: AlQuranDatabase) {
    val queries get() = database.playlistItemQueries
}
