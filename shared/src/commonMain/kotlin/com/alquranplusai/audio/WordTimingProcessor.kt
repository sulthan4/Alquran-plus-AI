package com.alquranplusai.audio

import com.alquranplusai.domain.models.WordTiming
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WordTimingProcessor {
    private val _currentActiveWordIndex = MutableStateFlow<Int?>(null)
    val currentActiveWordIndex: StateFlow<Int?> = _currentActiveWordIndex

    fun updatePosition(positionMs: Long, timings: List<WordTiming>) {
        val activeWord = timings.indexOfFirst { 
            positionMs >= it.startTime && positionMs < it.endTime 
        }
        if (activeWord != -1) {
            _currentActiveWordIndex.value = activeWord
        } else {
            _currentActiveWordIndex.value = null
        }
    }
}
