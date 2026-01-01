package com.alquranplusai.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class ImageLoader(private val context: Context) {

    companion object {
        private const val CACHE_DIR_NAME = "image_cache"
        private const val MAX_CACHE_SIZE_MB = 50
    }

    private val memoryCache = ConcurrentHashMap<String, Bitmap>()
    private val cacheDir = File(context.cacheDir, CACHE_DIR_NAME).apply { mkdirs() }

    suspend fun loadImage(url: String, imageView: ImageView) = withContext(Dispatchers.Main) {
        val cached = getFromMemoryCache(url) ?: getFromDiskCache(url)
        
        if (cached != null) {
            imageView.setImageBitmap(cached)
        } else {
            val bitmap = downloadImage(url)
            if (bitmap != null) {
                saveToCache(url, bitmap)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    suspend fun loadImageBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        getFromMemoryCache(url) ?: getFromDiskCache(url) ?: downloadImage(url)?.also {
            saveToCache(url, it)
        }
    }

    private fun getFromMemoryCache(url: String): Bitmap? {
        return memoryCache[getCacheKey(url)]
    }

    private suspend fun getFromDiskCache(url: String): Bitmap? = withContext(Dispatchers.IO) {
        val cacheFile = File(cacheDir, getCacheKey(url))
        if (cacheFile.exists()) {
            try {
                BitmapFactory.decodeFile(cacheFile.absolutePath)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    private suspend fun downloadImage(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun saveToCache(url: String, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val key = getCacheKey(url)
        
        memoryCache[key] = bitmap
        
        val cacheFile = File(cacheDir, key)
        try {
            FileOutputStream(cacheFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            }
        } catch (e: Exception) {
            // Ignore cache save errors
        }
        
        cleanupCacheIfNeeded()
    }

    private fun getCacheKey(url: String): String {
        return url.hashCode().toString()
    }

    private fun cleanupCacheIfNeeded() {
        val cacheSize = cacheDir.walkTopDown()
            .filter { it.isFile }
            .map { it.length() }
            .sum()
        
        val maxSize = MAX_CACHE_SIZE_MB * 1024 * 1024L
        
        if (cacheSize > maxSize) {
            val files = cacheDir.listFiles()?.sortedBy { it.lastModified() } ?: return
            var currentSize = cacheSize
            
            for (file in files) {
                if (currentSize <= maxSize * 0.8) break
                currentSize -= file.length()
                file.delete()
                memoryCache.remove(file.name)
            }
        }
    }

    fun clearCache() {
        memoryCache.clear()
        cacheDir.deleteRecursively()
        cacheDir.mkdirs()
    }

    fun clearMemoryCache() {
        memoryCache.clear()
    }

    fun getCacheSize(): Long {
        return cacheDir.walkTopDown()
            .filter { it.isFile }
            .map { it.length() }
            .sum()
    }
}
