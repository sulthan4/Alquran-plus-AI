package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.alquranplusai.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for PlayerViewModel
 */
class PlayerViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // TODO: Add ViewModel logic
}
