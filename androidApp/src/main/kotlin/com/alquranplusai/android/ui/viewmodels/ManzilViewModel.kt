package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManzilViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {
    
    private val _manzilNumber = MutableStateFlow(1)
    val manzilNumber: StateFlow<Int> = _manzilNumber.asStateFlow()
    
    private val _ayahs = MutableStateFlow<List<com.alquranplusai.domain.models.Ayah>>(emptyList())
    val ayahs: StateFlow<List<com.alquranplusai.domain.models.Ayah>> = _ayahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadManzil(manzilNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _manzilNumber.value = manzilNumber
            try {
                val manzilRanges = getManzilRanges(manzilNumber)
                val ayahs = mutableListOf<com.alquranplusai.domain.models.Ayah>()
                for (range in manzilRanges) {
                    quranRepository.getAyahsBySurah(range.first).collect { surahAyahs ->
                        ayahs.addAll(surahAyahs.filter { it.ayahNumber in range.second })
                    }
                }
                _ayahs.value = ayahs
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun getManzilRanges(manzilNumber: Int): List<Pair<Int, IntRange>> {
        return when (manzilNumber) {
            1 -> listOf(1 to 1..7, 2 to 1..141)
            2 -> listOf(2 to 142..252, 3 to 1..92)
            3 -> listOf(3 to 93..200, 4 to 1..23)
            4 -> listOf(4 to 24..147, 5 to 1..81)
            5 -> listOf(5 to 82..120, 6 to 1..110)
            6 -> listOf(6 to 111..165, 7 to 1..87)
            7 -> listOf(7 to 88..206, 8 to 1..75, 9 to 1..93)
            else -> emptyList()
        }
    }
    
    fun nextManzil() {
        if (_manzilNumber.value < 7) {
            loadManzil(_manzilNumber.value + 1)
        }
    }
    
    fun previousManzil() {
        if (_manzilNumber.value > 1) {
            loadManzil(_manzilNumber.value - 1)
        }
    }
}

