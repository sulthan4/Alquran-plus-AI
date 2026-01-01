package com.alquranplusai.domain.repositories

import com.alquranplusai.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository for user management and authentication
 */
interface UserRepository {
    // Authentication
    suspend fun login(email: String, password: String): Flow<Resource<AuthToken>>
    suspend fun register(email: String, password: String, name: String): Flow<Resource<AuthToken>>
    suspend fun loginWithGoogle(): Flow<Resource<AuthToken>>
    suspend fun loginWithFacebook(): Flow<Resource<AuthToken>>
    suspend fun logout()
    suspend fun refreshToken(refreshToken: String): Flow<Resource<AuthToken>>
    suspend fun resetPassword(email: String): Flow<Resource<Unit>>
    
    // User profile
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun getUserProfile(): Flow<Resource<UserProfile>>
    suspend fun updateProfile(profile: UserProfile): Flow<Resource<Unit>>
    suspend fun updateAvatar(imageData: ByteArray): Flow<Resource<String>>
    suspend fun deleteAccount(): Flow<Resource<Unit>>
    
    // Preferences
    suspend fun getPreferences(): Flow<UserPreferences>
    suspend fun updatePreferences(preferences: UserPreferences)
    suspend fun updateReadingPreferences(preferences: ReadingPreferences)
    suspend fun updateAudioPreferences(preferences: AudioPreferences)
    suspend fun updateUIPreferences(preferences: UIPreferences)
    suspend fun updateNotificationPreferences(preferences: NotificationPreferences)
    
    // Subscription
    suspend fun getSubscription(): Flow<Subscription?>
    suspend fun subscribe(tier: SubscriptionPlan): Flow<Resource<Subscription>>
    suspend fun cancelSubscription(): Flow<Resource<Unit>>
    suspend fun restorePurchases(): Flow<Resource<Unit>>
    
    // Data sync
    suspend fun backupData(): Flow<Resource<Unit>>
    suspend fun restoreData(): Flow<Resource<Unit>>
    suspend fun syncData(): Flow<Resource<Unit>>
    suspend fun getLastSyncTime(): Flow<Long?>
    
    // Session
    suspend fun isLoggedIn(): Flow<Boolean>
    suspend fun getAuthToken(): Flow<AuthToken?>
    suspend fun saveAuthToken(token: AuthToken)
    suspend fun clearAuthToken()
}
