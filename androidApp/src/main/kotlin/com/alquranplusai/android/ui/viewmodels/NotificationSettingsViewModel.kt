package com.alquranplusai.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Using shared preferences
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationSettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()
    
    private val _dailyReminderTime = MutableStateFlow("09:00")
    val dailyReminderTime: StateFlow<String> = _dailyReminderTime.asStateFlow()
    
    init {
        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { _notificationsEnabled.value = it }
        }
        viewModelScope.launch {
            preferencesManager.dailyReminderTime.collect { _dailyReminderTime.value = it }
        }
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateNotificationsEnabled(enabled)
            _notificationsEnabled.value = enabled
        }
    }
    
    fun setDailyReminderTime(time: String) {
        viewModelScope.launch {
            preferencesManager.updateDailyReminderTime(time)
            _dailyReminderTime.value = time
        }
    }
}

