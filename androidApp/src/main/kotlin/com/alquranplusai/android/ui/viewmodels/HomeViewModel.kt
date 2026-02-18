package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.models.ReadingSession
import com.alquranplusai.domain.repositories.QuranRepository
import com.alquranplusai.domain.repositories.AnalyticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val quranRepository: QuranRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    
    // State for UI binding
    data class LastReadUiState(
        val surahName: String = "",
        val surahNumber: Int = 1,
        val ayahNumber: Int = 1,
        val totalAyahs: Int = 7
    )

    private val _lastReadUiState = MutableStateFlow<LastReadUiState?>(null)
    val lastReadUiState: StateFlow<LastReadUiState?> = _lastReadUiState.asStateFlow()
    
    // Keep raw position for navigation
    private val _lastReadPosition = MutableStateFlow<Pair<Int, Int>?>(null)
    val lastReadPosition: StateFlow<Pair<Int, Int>?> = _lastReadPosition.asStateFlow()
    
    private val _dailyVerse = MutableStateFlow<Pair<Surah, Int>?>(null)
    val dailyVerse: StateFlow<Pair<Surah, Int>?> = _dailyVerse.asStateFlow()

    private val _userName = MutableStateFlow("Mohamed")
    val userName: StateFlow<String> = _userName.asStateFlow()
    
    private val _readingStreak = MutableStateFlow(0)
    val readingStreak: StateFlow<Int> = _readingStreak.asStateFlow()
    
    private val _totalReadingTime = MutableStateFlow(0L)
    val totalReadingTime: StateFlow<Long> = _totalReadingTime.asStateFlow()
    
    private val _completedSurahs = MutableStateFlow(0)
    val completedSurahs: StateFlow<Int> = _completedSurahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Init block MUST be after property declarations to avoid NullPointerException
    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        val userId = "current_user" 
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Launch separate collectors for each stat to avoid blocking
                launch {
                    quranRepository.getLastReadingPosition().collect { position ->
                        _lastReadPosition.value = position
                        if (position != null) {
                            quranRepository.getSurahByNumber(position.first).collect { surah ->
                                if (surah != null) {
                                    _lastReadUiState.value = LastReadUiState(
                                        surahName = surah.nameTransliteration,
                                        surahNumber = surah.number,
                                        ayahNumber = position.second,
                                        totalAyahs = surah.numberOfAyahs
                                    )
                                }
                            }
                        } else {
                            _lastReadUiState.value = null
                        }
                    }
                }
                
                launch {
                    analyticsRepository.getCurrentStreak(userId).collect { streak ->
                        _readingStreak.value = streak
                    }
                }
                
                launch {
                    analyticsRepository.getTotalReadingTime(userId).collect { time ->
                        _totalReadingTime.value = time
                    }
                }
                
                launch {
                    quranRepository.getCompletedSurahs().collect { completed ->
                        _completedSurahs.value = completed.size
                    }
                }
                
                loadDailyVerse()
            } catch (e: Exception) {
                println("Error loading home data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadDailyVerse() {
        viewModelScope.launch {
            try {
                // Determine a daily verse based on the day of the year to be consistent for the whole day
                val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
                // Random deterministic selection
                // Seed random with dayOfYear to get same surah for everyone on the same day
                val random = java.util.Random(dayOfYear.toLong())
                val surahNumber = random.nextInt(114) + 1
                val ayahNumber = 1 // Simplified: always first ayah for now
                
                quranRepository.getSurahByNumber(surahNumber).collect { surah ->
                    if (surah != null) {
                        _dailyVerse.value = Pair(surah, ayahNumber)
                    }
                }
            } catch (e: Exception) {
                println("Error loading daily verse: ${e.message}")
            }
        }
    }
    
    fun continueReading() {
        // Navigation will be handled by the screen
    }
    
    fun refresh() {
        loadHomeData()
    }
}
