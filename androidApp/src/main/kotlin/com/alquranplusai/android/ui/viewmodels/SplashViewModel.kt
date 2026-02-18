package com.alquranplusai.android.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val context: Context
) : ViewModel() {

    companion object {
        const val PREFS_NAME = "alquran_prefs"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkOnboardingState()
    }

    private fun checkOnboardingState() {
        viewModelScope.launch {
            // Artificial delay for splash branding
            delay(1500)
            
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val onboardingCompleted = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
            
            _destination.value = if (onboardingCompleted) {
                SplashDestination.Home
            } else {
                SplashDestination.Onboarding
            }
        }
    }

    sealed class SplashDestination {
        object Home : SplashDestination()
        object Onboarding : SplashDestination()
    }
}
