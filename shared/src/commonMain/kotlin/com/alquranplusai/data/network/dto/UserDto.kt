package com.alquranplusai.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    val displayName: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: Long,
    val lastLoginAt: Long? = null
)

@Serializable
data class UserProfileDto(
    val userId: Int,
    val bio: String? = null,
    val location: String? = null,
    val preferredLanguage: String = "en",
    val preferredTranslation: Int? = null,
    val preferredReciter: Int? = null
)

@Serializable
data class SubscriptionDto(
    val id: Int,
    val userId: Int,
    val plan: String,
    val status: String,
    val startDate: Long,
    val endDate: Long? = null
)
