package com.alquranplusai.data.database.dao
import com.alquranplusai.data.database.AlQuranDatabase
class QuestionDao(private val database: AlQuranDatabase) { val queries get() = database.questionQueries }
