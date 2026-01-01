package com.alquranplusai.data.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Speech Recognition Engine for voice search
 */
class SpeechRecognitionEngine(private val context: Context) {
    
    private var speechRecognizer: SpeechRecognizer? = null
    
    /**
     * Check if speech recognition is available
     */
    fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }
    
    /**
     * Start listening for speech
     */
    fun startListening(languageCode: String = "en-US"): Flow<SpeechRecognitionResult> = callbackFlow {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                trySend(SpeechRecognitionResult.Ready)
            }
            
            override fun onBeginningOfSpeech() {
                trySend(SpeechRecognitionResult.Speaking)
            }
            
            override fun onRmsChanged(rmsdB: Float) {
                trySend(SpeechRecognitionResult.VolumeChanged(rmsdB))
            }
            
            override fun onBufferReceived(buffer: ByteArray?) {}
            
            override fun onEndOfSpeech() {
                trySend(SpeechRecognitionResult.EndOfSpeech)
            }
            
            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
                    else -> "Unknown error"
                }
                trySend(SpeechRecognitionResult.Error(errorMessage))
                close()
            }
            
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val confidences = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
                
                if (matches != null && matches.isNotEmpty()) {
                    val recognizedTexts = matches.mapIndexed { index, text ->
                        RecognizedText(text, confidences?.getOrNull(index) ?: 0f)
                    }
                    trySend(SpeechRecognitionResult.Success(recognizedTexts))
                }
                close()
            }
            
            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    trySend(SpeechRecognitionResult.PartialResult(matches.first()))
                }
            }
            
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        
        speechRecognizer?.setRecognitionListener(recognitionListener)
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        }
        
        speechRecognizer?.startListening(intent)
        
        awaitClose {
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }
    
    /**
     * Stop listening
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
    }
    
    /**
     * Cancel recognition
     */
    fun cancel() {
        speechRecognizer?.cancel()
    }
    
    data class RecognizedText(
        val text: String,
        val confidence: Float
    )
    
    sealed class SpeechRecognitionResult {
        object Ready : SpeechRecognitionResult()
        object Speaking : SpeechRecognitionResult()
        object EndOfSpeech : SpeechRecognitionResult()
        data class VolumeChanged(val volume: Float) : SpeechRecognitionResult()
        data class PartialResult(val text: String) : SpeechRecognitionResult()
        data class Success(val results: List<RecognizedText>) : SpeechRecognitionResult()
        data class Error(val message: String) : SpeechRecognitionResult()
    }
}
