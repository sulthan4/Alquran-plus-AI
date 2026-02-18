package com.alquranplusai.android.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val context: Context
) : ViewModel() {
    
    companion object {
        const val PREFS_NAME = "alquran_prefs"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
    
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()
    
    fun nextPage() {
        _currentPage.value++
    }
    
    fun previousPage() {
        if (_currentPage.value > 0) {
            _currentPage.value--
        }
    }
    
    fun completeOnboarding() {
        viewModelScope.launch {
            // Persist onboarding completion
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
            
            _isCompleted.value = true
        }
    }
}
