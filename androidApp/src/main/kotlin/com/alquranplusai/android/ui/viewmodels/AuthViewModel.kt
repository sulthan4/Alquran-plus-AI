package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: com.alquranplusai.domain.repositories.UserRepository
) : ViewModel() {
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.login(email, password)
                _isAuthenticated.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
                _isAuthenticated.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                userRepository.register(email, password, name)
                _isAuthenticated.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Registration failed"
                _isAuthenticated.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
                _isAuthenticated.value = false
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

