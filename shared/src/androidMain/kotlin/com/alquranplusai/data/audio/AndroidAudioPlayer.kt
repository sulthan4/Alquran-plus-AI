package com.alquranplusai.data.audio

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    private val _currentPosition = MutableStateFlow(0L)
    
    init {
        scope.launch {
            initController()
            startIndexer()
        }
    }

    private suspend fun initController() {
        if (mediaController != null) return

        val sessionToken = SessionToken(
            context,
            ComponentName(context, "com.alquranplusai.android.services.AudioPlaybackService")
        )
        
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        
        try {
            mediaController = withContext(Dispatchers.IO) {
                mediaControllerFuture?.get()
            }
            
            mediaController?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    updatePlaybackState(playbackState)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        _playbackState.value = PlaybackState.PLAYING
                    } else if (mediaController?.playbackState == Player.STATE_READY) {
                        _playbackState.value = PlaybackState.PAUSED
                    }
                }
                
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                }
            })
            
            mediaController?.let {
                updatePlaybackState(it.playbackState)
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePlaybackState(state: Int) {
        _playbackState.value = when (state) {
            Player.STATE_IDLE -> PlaybackState.IDLE
            Player.STATE_BUFFERING -> PlaybackState.PLAYING
            Player.STATE_READY -> if (mediaController?.isPlaying == true) PlaybackState.PLAYING else PlaybackState.PAUSED
            Player.STATE_ENDED -> PlaybackState.STOPPED
            else -> PlaybackState.IDLE
        }
    }
    
    private fun startIndexer() {
        scope.launch {
            while (isActive) {
                mediaController?.let { player ->
                    if (player.isPlaying) {
                        _currentPosition.value = player.currentPosition
                    }
                }
                delay(100) 
            }
        }
    }

    override fun getPlaybackState(): Flow<PlaybackState> = _playbackState.asStateFlow()

    override fun getCurrentPosition(): Flow<Long> = _currentPosition.asStateFlow()

    override suspend fun play(url: String) {
        // Ensure controller is ready
        if (mediaController == null) {
            initController()
        }
        
        withContext(Dispatchers.Main) {
            mediaController?.let { player ->
                val mediaItem = MediaItem.fromUri(url)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            }
        }
    }

    override suspend fun pause() {
        withContext(Dispatchers.Main) {
            mediaController?.pause()
        }
    }

    override suspend fun resume() {
        withContext(Dispatchers.Main) {
            mediaController?.play()
        }
    }

    override suspend fun stop() {
        withContext(Dispatchers.Main) {
            mediaController?.stop()
            mediaController?.clearMediaItems()
            _playbackState.value = PlaybackState.STOPPED
            _currentPosition.value = 0L
        }
    }

    override suspend fun seekTo(position: Long) {
        withContext(Dispatchers.Main) {
            mediaController?.seekTo(position)
        }
    }

    override suspend fun setSpeed(speed: Float) {
        withContext(Dispatchers.Main) {
            mediaController?.setPlaybackSpeed(speed)
        }
    }

    override suspend fun setVolume(volume: Float) {
        withContext(Dispatchers.Main) {
            mediaController?.volume = volume
        }
    }
    
    fun release() {
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        scope.cancel()
    }
}
