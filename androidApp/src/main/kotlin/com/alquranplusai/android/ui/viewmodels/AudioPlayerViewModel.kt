package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.audio.AudioPlayer
import com.alquranplusai.data.audio.PlaybackState
import com.alquranplusai.domain.models.AudioFile
import com.alquranplusai.domain.models.Ayah
import com.alquranplusai.domain.models.Reciter
import com.alquranplusai.domain.repositories.AudioRepository
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioRepository: AudioRepository,
    private val quranRepository: QuranRepository,
    private val audioPlayer: AudioPlayer,
    private val preferencesManager: com.alquranplusai.data.preferences.PreferencesManager
) : ViewModel() {
    
    // Reciter management
    private val _reciters = MutableStateFlow<List<Reciter>>(emptyList())
    val reciters: StateFlow<List<Reciter>> = _reciters.asStateFlow()
    
    private val _selectedReciter = MutableStateFlow<Reciter?>(null)
    val selectedReciter: StateFlow<Reciter?> = _selectedReciter.asStateFlow()
    
    // Playback state
    val isPlaying: StateFlow<Boolean> = audioPlayer.getPlaybackState()
        .map { it == PlaybackState.PLAYING }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _currentAyah = MutableStateFlow<Ayah?>(null)
    val currentAyah: StateFlow<Ayah?> = _currentAyah.asStateFlow()
    
    val currentPosition: StateFlow<Long> = audioPlayer.getCurrentPosition()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)
    
    // Playback controls
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()
        
    // Playlist
    private val _playlist = MutableStateFlow<List<Ayah>>(emptyList())
    
    // Timings
    private val _currentAudioFile = MutableStateFlow<AudioFile?>(null)
    
    // Sleep Timer
    private val _sleepTimerRemaining = MutableStateFlow<Long?>(null)
    val sleepTimerRemaining: StateFlow<Long?> = _sleepTimerRemaining.asStateFlow()
    private var sleepTimerJob: Job? = null
    
    // Repeat Mode
    enum class RepeatMode { OFF, AYAH, SURAH, RANGE, ALL }
    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()
    private val _repeatRange = MutableStateFlow<Pair<Int, Int>?>(null)
    val repeatRange: StateFlow<Pair<Int, Int>?> = _repeatRange.asStateFlow()
    
    // Hifz Mode (Memorization)
    private val _hifzMode = MutableStateFlow(false)
    val hifzMode: StateFlow<Boolean> = _hifzMode.asStateFlow()
    private val _hifzRepeatCount = MutableStateFlow(3)
    val hifzRepeatCount: StateFlow<Int> = _hifzRepeatCount.asStateFlow()
    private val _hifzCurrentRepeat = MutableStateFlow(0)
    val hifzCurrentRepeat: StateFlow<Int> = _hifzCurrentRepeat.asStateFlow()
    
    val activeSurahNumber: StateFlow<Int?> = _currentAudioFile.map { it?.surahNumber }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val activeWordPosition: StateFlow<Int?> = combine(
        currentPosition,
        _currentAudioFile
    ) { position: Long, audioFile: AudioFile? ->
        val timing = audioFile?.wordTimings?.find { timing ->
            position >= timing.startTime && position < timing.endTime 
        }
        
        if (timing != null) {
            println("AlQuranPlusAI: Reciting Ayah ${timing.verseNumber}, Word ${timing.wordPosition}")
        }
        
        timing?.wordPosition
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val activeAyahNumber: StateFlow<Int?> = combine(
        currentPosition,
        _currentAudioFile
    ) { position: Long, audioFile: AudioFile? ->
        audioFile?.wordTimings?.find { timing ->
            position >= timing.startTime && position < timing.endTime 
        }?.verseNumber
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Update currentAyah dynamically based on playback position
    init {
        loadReciters()
        observeSettings()
        
        // Dynamic Ayah tracking
        viewModelScope.launch {
            activeAyahNumber.collect { ayahNum ->
                if (ayahNum != null && _currentAyah.value?.ayahNumber != ayahNum) {
                    val surahNum = activeSurahNumber.value ?: _currentAyah.value?.surahNumber ?: return@collect
                    quranRepository.getAyahByNumber(surahNum, ayahNum).collect { ayah ->
                        if (ayah != null) {
                            _currentAyah.value = ayah
                        }
                    }
                }
            }
        }
    }
    
    private fun observeSettings() {
        viewModelScope.launch {
            preferencesManager.playbackSpeed.collect { speed ->
                _playbackSpeed.value = speed
                audioPlayer.setSpeed(speed)
            }
        }
    }
    
    private fun loadReciters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                audioRepository.getAllReciters().collect { reciterList ->
                    _reciters.value = reciterList
                    if (_selectedReciter.value == null && reciterList.isNotEmpty()) {
                        // Default to Mishary Rashid Alafasy (ID 7) if available, otherwise first sync-capable or first reciter
                        val defaultReciter = reciterList.find { it.id == "7" }
                            ?: reciterList.find { it.hasWordTiming }
                            ?: reciterList.first()
                        _selectedReciter.value = defaultReciter
                    }
                }
            } catch (e: Exception) {
                // Log error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectReciter(reciter: Reciter) {
        val wasPlaying = isPlaying.value
        _selectedReciter.value = reciter
        
        // If we were playing or had a file loaded, refresh with new reciter
        if (wasPlaying || _currentAudioFile.value != null) {
            viewModelScope.launch {
                // If it was playing, we want to restart playback immediately with new reciter
                // If it was paused/idle, we just reload the file source so next play uses new reciter
                playCurrentSelection() 
                if (!wasPlaying) {
                    audioPlayer.pause() // Keep it paused if it was paused, but file is now updated
                }
            }
        }
    }
    
    fun skipNext() {
        // Simple logic: Go to next Surah (1..114)
        val currentSurah = _currentAudioFile.value?.surahNumber ?: _currentAyah.value?.surahNumber ?: 1
        if (currentSurah < 114) {
            playCurrentSelection(currentSurah + 1)
        }
    }

    fun skipPrevious() {
        val currentSurah = _currentAudioFile.value?.surahNumber ?: _currentAyah.value?.surahNumber ?: 1
        if (currentSurah > 1) {
            playCurrentSelection(currentSurah - 1)
        }
    }
    
    fun updatePlaybackContext(ayah: Ayah) {
        _currentAyah.value = ayah
    }
    
    fun togglePlayPause(surahNumber: Int? = null) {
        viewModelScope.launch {
            val currentState = isPlaying.value
            if (currentState) {
                audioPlayer.pause()
            } else {
                val playerState = audioPlayer.getPlaybackState().stateIn(this).value
                if (playerState == PlaybackState.PAUSED && surahNumber == null) {
                    // Only resume if we are not requesting a specific new surah
                    audioPlayer.resume()
                } else {
                    playCurrentSelection(surahNumber)
                }
            }
        }
    }
    
    private fun playCurrentSelection(requestedSurahNumber: Int? = null) {
        val reciter = _selectedReciter.value ?: return
        val surahNumber = requestedSurahNumber ?: _currentAyah.value?.surahNumber ?: 1
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                audioRepository.getAudioFile(reciter.id, surahNumber, null).collect { audioFile ->
                    if (audioFile != null) {
                        println("AlQuranPlusAI: Starting playback of ${audioFile.url}")
                        println("AlQuranPlusAI: Reciter ${reciter.name} (hasTimings: ${reciter.hasWordTiming})")
                        println("AlQuranPlusAI: Timings synced: ${audioFile.wordTimings.size}")
                        
                        _currentAudioFile.value = audioFile
                        audioPlayer.play(audioFile.url)
                    } else {
                        println("AlQuranPlusAI: No audio file found for reciter ${reciter.name} and surah $surahNumber")
                    }
                }
            } catch (e: Exception) {
                println("AlQuranPlusAI: Error fetching audio file: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun seekTo(position: Long) {
        viewModelScope.launch {
            audioPlayer.seekTo(position)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            preferencesManager.updatePlaybackSpeed(speed)
        }
    }
    
    // Sleep Timer Functions
    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        
        if (minutes > 0) {
            val endTime = System.currentTimeMillis() + (minutes * 60 * 1000)
            
            sleepTimerJob = viewModelScope.launch {
                while (System.currentTimeMillis() < endTime) {
                    _sleepTimerRemaining.value = endTime - System.currentTimeMillis()
                    delay(1000)
                }
                
                // Timer expired - stop playback
                audioPlayer.pause()
                _sleepTimerRemaining.value = null
            }
        } else {
            _sleepTimerRemaining.value = null
        }
    }
    
    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimerRemaining.value = null
    }
    
    // Repeat Mode Functions
    fun cycleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.AYAH
            RepeatMode.AYAH -> RepeatMode.SURAH
            RepeatMode.SURAH -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.OFF
            RepeatMode.RANGE -> RepeatMode.OFF
        }
    }
    
    fun setRepeatRange(startAyah: Int, endAyah: Int) {
        _repeatRange.value = Pair(startAyah, endAyah)
        _repeatMode.value = RepeatMode.RANGE
    }
    
    fun clearRepeatRange() {
        _repeatRange.value = null
        if (_repeatMode.value == RepeatMode.RANGE) {
            _repeatMode.value = RepeatMode.OFF
        }
    }
    
    // Hifz Mode Functions
    fun toggleHifzMode() {
        _hifzMode.value = !_hifzMode.value
        if (!_hifzMode.value) {
            _hifzCurrentRepeat.value = 0
        }
    }
    
    fun setHifzRepeatCount(count: Int) {
        _hifzRepeatCount.value = count
    }
    
    override fun onCleared() {
        super.onCleared()
        sleepTimerJob?.cancel()
    }
}
