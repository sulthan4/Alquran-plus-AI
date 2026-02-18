package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.data.services.DataLoadingResult
import com.alquranplusai.data.services.LoadingProgress
import com.alquranplusai.data.services.QuranDataLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Quran data loading from API
 */
class DataLoadingViewModel(
    private val dataLoader: QuranDataLoader
) : ViewModel() {
    
    private val _loadingState = MutableStateFlow<DataLoadingResult>(
        DataLoadingResult.Loading(0, 114)
    )
    val loadingState: StateFlow<DataLoadingResult> = _loadingState.asStateFlow()
    
    private val _progress = MutableStateFlow(
        LoadingProgress(
            loadedVersesCount = 0,
            totalVersesCount = 6236,
            percentage = 0,
            isComplete = false
        )
    )
    val progress: StateFlow<LoadingProgress> = _progress.asStateFlow()
    
    /**
     * Check if data is already loaded
     */
    suspend fun isDataLoaded(): Boolean {
        return dataLoader.isDataLoaded()
    }
    
    /**
     * Start loading Quran data from API
     */
    fun loadQuranData() {
        viewModelScope.launch {
            dataLoader.loadCompleteQuran { current, total ->
                // Update progress as surahs are loaded
                viewModelScope.launch {
                    _progress.value = dataLoader.getLoadingProgress()
                }
            }.collect { result ->
                _loadingState.value = result
                
                // Update progress
                if (result is DataLoadingResult.Loading || result is DataLoadingResult.Success) {
                    _progress.value = dataLoader.getLoadingProgress()
                }
            }
        }
    }
}
