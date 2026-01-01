package com.alquranplusai.data.database.dao

import com.alquranplusai.data.database.AlQuranDatabase

class PlaylistDao(private val database: AlQuranDatabase) {
    val queries get() = database.playlistQueries
}
