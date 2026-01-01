package com.alquranplusai.android.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager as AndroidAudioManager
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AudioManager(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: AudioManager? = null
        
        fun getInstance(context: Context): AudioManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AudioManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AndroidAudioManager
    
    private val _hasAudioFocus = MutableStateFlow(false)
    val hasAudioFocus: StateFlow<Boolean> = _hasAudioFocus
    
    private val _volume = MutableStateFlow(getCurrentVolume())
    val volume: StateFlow<Int> = _volume
    
    private var audioFocusRequest: AudioFocusRequest? = null

    private val audioFocusChangeListener = AndroidAudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AndroidAudioManager.AUDIOFOCUS_GAIN -> {
                _hasAudioFocus.value = true
                onAudioFocusGained()
            }
            AndroidAudioManager.AUDIOFOCUS_LOSS -> {
                _hasAudioFocus.value = false
                onAudioFocusLost()
            }
            AndroidAudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                _hasAudioFocus.value = false
                onAudioFocusLostTransient()
            }
            AndroidAudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                onAudioFocusLostTransientCanDuck()
            }
        }
    }

    fun requestAudioFocus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            
            audioFocusRequest = AudioFocusRequest.Builder(AndroidAudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build()
            
            val result = audioManager.requestAudioFocus(audioFocusRequest!!)
            val granted = result == AndroidAudioManager.AUDIOFOCUS_REQUEST_GRANTED
            _hasAudioFocus.value = granted
            granted
        } else {
            @Suppress("DEPRECATION")
            val result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AndroidAudioManager.STREAM_MUSIC,
                AndroidAudioManager.AUDIOFOCUS_GAIN
            )
            val granted = result == AndroidAudioManager.AUDIOFOCUS_REQUEST_GRANTED
            _hasAudioFocus.value = granted
            granted
        }
    }

    fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let {
                audioManager.abandonAudioFocusRequest(it)
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        }
        _hasAudioFocus.value = false
    }

    fun getCurrentVolume(): Int {
        return audioManager.getStreamVolume(AndroidAudioManager.STREAM_MUSIC)
    }

    fun getMaxVolume(): Int {
        return audioManager.getStreamMaxVolume(AndroidAudioManager.STREAM_MUSIC)
    }

    fun setVolume(volume: Int) {
        audioManager.setStreamVolume(
            AndroidAudioManager.STREAM_MUSIC,
            volume.coerceIn(0, getMaxVolume()),
            0
        )
        _volume.value = getCurrentVolume()
    }

    fun increaseVolume() {
        audioManager.adjustStreamVolume(
            AndroidAudioManager.STREAM_MUSIC,
            AndroidAudioManager.ADJUST_RAISE,
            AndroidAudioManager.FLAG_SHOW_UI
        )
        _volume.value = getCurrentVolume()
    }

    fun decreaseVolume() {
        audioManager.adjustStreamVolume(
            AndroidAudioManager.STREAM_MUSIC,
            AndroidAudioManager.ADJUST_LOWER,
            AndroidAudioManager.FLAG_SHOW_UI
        )
        _volume.value = getCurrentVolume()
    }

    fun isMuted(): Boolean {
        return getCurrentVolume() == 0
    }

    fun mute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.adjustStreamVolume(
                AndroidAudioManager.STREAM_MUSIC,
                AndroidAudioManager.ADJUST_MUTE,
                0
            )
        } else {
            @Suppress("DEPRECATION")
            audioManager.setStreamMute(AndroidAudioManager.STREAM_MUSIC, true)
        }
        _volume.value = 0
    }

    fun unmute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.adjustStreamVolume(
                AndroidAudioManager.STREAM_MUSIC,
                AndroidAudioManager.ADJUST_UNMUTE,
                0
            )
        } else {
            @Suppress("DEPRECATION")
            audioManager.setStreamMute(AndroidAudioManager.STREAM_MUSIC, false)
        }
        _volume.value = getCurrentVolume()
    }

    private fun onAudioFocusGained() {
    }

    private fun onAudioFocusLost() {
    }

    private fun onAudioFocusLostTransient() {
    }

    private fun onAudioFocusLostTransientCanDuck() {
    }
}
