package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.models.Surah
import com.alquranplusai.domain.models.RevelationType
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurahListViewModel(
    private val quranRepository: QuranRepository
) : ViewModel() {
    
    private val _surahs = MutableStateFlow<List<Surah>>(emptyList())
    val surahs: StateFlow<List<Surah>> = _surahs.asStateFlow()
    
    private val _filteredSurahs = MutableStateFlow<List<Surah>>(emptyList())
    val filteredSurahs: StateFlow<List<Surah>> = _filteredSurahs.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedRevelationType = MutableStateFlow<RevelationType?>(null)
    val selectedRevelationType: StateFlow<RevelationType?> = _selectedRevelationType.asStateFlow()
    
    init {
        loadSurahs()
    }
    
    private fun loadSurahs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                quranRepository.getAllSurahs().collect { surahList ->
                    _surahs.value = surahList
                    _filteredSurahs.value = surahList
                }
            } catch (e: Exception) {
                // TODO: Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchSurahs(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            try {
                quranRepository.searchSurahs(query).collect { results ->
                    _filteredSurahs.value = results
                }
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
    
    fun filterByRevelationType(type: RevelationType?) {
        _selectedRevelationType.value = type
        if (type == null) {
            _filteredSurahs.value = _surahs.value
        } else {
            viewModelScope.launch {
                try {
                    quranRepository.getSurahsByRevelationType(type).collect { results ->
                        _filteredSurahs.value = results
                    }
                } catch (e: Exception) {
                    // TODO: Handle error
                }
            }
        }
    }
    
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedRevelationType.value = null
        _filteredSurahs.value = _surahs.value
    }
}
