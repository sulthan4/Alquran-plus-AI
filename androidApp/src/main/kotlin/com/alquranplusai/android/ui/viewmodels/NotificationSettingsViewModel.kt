package com.alquranplusai.android.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alquranplusai.android.workers.ReminderWorker
import com.alquranplusai.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * ViewModel for managing notification settings and scheduling reminders.
 */
class NotificationSettingsViewModel(
    private val context: Context,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()
    
    private val _dailyReminderTime = MutableStateFlow("09:00")
    val dailyReminderTime: StateFlow<String> = _dailyReminderTime.asStateFlow()
    
    init {
        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { 
                _notificationsEnabled.value = it 
            }
        }
        viewModelScope.launch {
            preferencesManager.dailyReminderTime.collect { 
                _dailyReminderTime.value = it 
            }
        }
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateNotificationsEnabled(enabled)
            _notificationsEnabled.value = enabled
            updateScheduling()
        }
    }
    
    fun setDailyReminderTime(time: String) {
        viewModelScope.launch {
            preferencesManager.updateDailyReminderTime(time)
            _dailyReminderTime.value = time
            updateScheduling()
        }
    }

    private fun updateScheduling() {
        if (_notificationsEnabled.value) {
            val timeParts = _dailyReminderTime.value.split(":")
            if (timeParts.size == 2) {
                val hour = timeParts[0].toIntOrNull() ?: 9
                val minute = timeParts[1].toIntOrNull() ?: 0
                ReminderWorker.scheduleReminder(context, hour, minute)
            }
        } else {
            ReminderWorker.cancelReminder(context)
        }
    }
}
