package com.alquranplusai.data.audio

/**
 * Audio cache manager
 */
class AudioCache {
    
    private val cache = mutableMapOf<String, ByteArray>()
    
    suspend fun cacheAudio(url: String, data: ByteArray) {
        cache[url] = data
    }
    
    suspend fun getCachedAudio(url: String): ByteArray? {
        return cache[url]
    }
    
    suspend fun clearCache() {
        cache.clear()
    }
    
    suspend fun getCacheSize(): Long {
        return cache.values.sumOf { it.size.toLong() }
    }
}
