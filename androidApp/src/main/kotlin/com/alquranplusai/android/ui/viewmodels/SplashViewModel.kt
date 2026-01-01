package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.domain.repositories.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            // Artificial delay for splash branding
            delay(1500)
            
            userRepository.isLoggedIn().collect { isLoggedIn ->
                if (isLoggedIn) {
                    _destination.value = SplashDestination.Home
                } else {
                    _destination.value = SplashDestination.Onboarding
                }
            }
        }
    }

    sealed class SplashDestination {
        object Home : SplashDestination()
        object Onboarding : SplashDestination()
    }
}
