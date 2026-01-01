package com.alquranplusai.android.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent

class MediaButtonReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON"
        
        private const val PREFS_NAME = "media_button_prefs"
        private const val KEY_LAST_CLICK_TIME = "last_click_time"
        private const val DOUBLE_CLICK_THRESHOLD = 300L
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_MEDIA_BUTTON) return
        
        val event = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT) ?: return
        
        if (event.action == KeyEvent.ACTION_DOWN) {
            handleMediaButton(context, event.keyCode)
        }
    }

    private fun handleMediaButton(context: Context, keyCode: Int) {
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY -> handlePlayButton(context)
            KeyEvent.KEYCODE_MEDIA_PAUSE -> handlePauseButton(context)
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> handlePlayPauseButton(context)
            KeyEvent.KEYCODE_MEDIA_NEXT -> handleNextButton(context)
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> handlePreviousButton(context)
            KeyEvent.KEYCODE_MEDIA_STOP -> handleStopButton(context)
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> handleFastForwardButton(context)
            KeyEvent.KEYCODE_MEDIA_REWIND -> handleRewindButton(context)
            KeyEvent.KEYCODE_HEADSETHOOK -> handleHeadsetHook(context)
        }
    }

    private fun handlePlayButton(context: Context) {
        broadcastMediaAction(context, "PLAY")
    }

    private fun handlePauseButton(context: Context) {
        broadcastMediaAction(context, "PAUSE")
    }

    private fun handlePlayPauseButton(context: Context) {
        broadcastMediaAction(context, "PLAY_PAUSE")
    }

    private fun handleNextButton(context: Context) {
        broadcastMediaAction(context, "NEXT")
    }

    private fun handlePreviousButton(context: Context) {
        broadcastMediaAction(context, "PREVIOUS")
    }

    private fun handleStopButton(context: Context) {
        broadcastMediaAction(context, "STOP")
    }

    private fun handleFastForwardButton(context: Context) {
        broadcastMediaAction(context, "FAST_FORWARD")
    }

    private fun handleRewindButton(context: Context) {
        broadcastMediaAction(context, "REWIND")
    }

    private fun handleHeadsetHook(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastClickTime = prefs.getLong(KEY_LAST_CLICK_TIME, 0)
        val currentTime = System.currentTimeMillis()
        
        if (currentTime - lastClickTime < DOUBLE_CLICK_THRESHOLD) {
            broadcastMediaAction(context, "NEXT")
        } else {
            broadcastMediaAction(context, "PLAY_PAUSE")
        }
        
        prefs.edit().putLong(KEY_LAST_CLICK_TIME, currentTime).apply()
    }

    private fun broadcastMediaAction(context: Context, action: String) {
        val intent = Intent("com.alquranplusai.android.MEDIA_ACTION").apply {
            putExtra("action", action)
            putExtra("timestamp", System.currentTimeMillis())
        }
        context.sendBroadcast(intent)
    }
}
