package com.alquranplusai.utils

import kotlinx.serialization.json.Json

object JsonUtils {
    
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
    }
    
    inline fun <reified T> fromJson(jsonString: String): T? {
        return try {
            json.decodeFromString<T>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
    
    inline fun <reified T> toJson(obj: T): String {
        return try {
            json.encodeToString(kotlinx.serialization.serializer(), obj)
        } catch (e: Exception) {
            ""
        }
    }
}
