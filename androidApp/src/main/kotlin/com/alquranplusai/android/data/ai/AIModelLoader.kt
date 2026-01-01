package com.alquranplusai.data.ai

import android.content.Context
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * AI Model Loader for TensorFlow Lite models
 */
class AIModelLoader(private val context: Context) {
    
    companion object {
        private const val MODEL_PATH = "models/"
        const val EMBEDDINGS_MODEL = "quran_embeddings.tflite"
        const val CLASSIFICATION_MODEL = "text_classifier.tflite"
    }
    
    /**
     * Load TFLite model from assets
     */
    fun loadModel(modelName: String): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd("$MODEL_PATH$modelName")
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    
    /**
     * Check if model exists in assets
     */
    fun modelExists(modelName: String): Boolean {
        return try {
            context.assets.open("$MODEL_PATH$modelName").close()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get model size in bytes
     */
    fun getModelSize(modelName: String): Long {
        return try {
            val assetFileDescriptor = context.assets.openFd("$MODEL_PATH$modelName")
            assetFileDescriptor.length
        } catch (e: Exception) {
            0L
        }
    }
}
