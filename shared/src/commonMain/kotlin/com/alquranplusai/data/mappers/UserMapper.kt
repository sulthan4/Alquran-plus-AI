package com.alquranplusai.data.mappers

import com.alquranplusai.data.network.dto.*
import com.alquranplusai.domain.models.*

/** Mapper for User data */
class UserMapper {

    fun mapUserDtoToDomain(dto: UserDto, profile: UserProfile, preferences: UserPreferences): User {
        return User(
                id = dto.id.toString(),
                email = dto.email,
                username = dto.username,
                profile = profile,
                preferences = preferences,
                createdAt = dto.createdAt,
                updatedAt = dto.createdAt,
                lastLoginAt = dto.lastLoginAt
        )
    }

    fun mapUserProfileDtoToDomain(dto: UserProfileDto, displayName: String?): UserProfile {
        return UserProfile(
                displayName = displayName ?: "",
                bio = dto.bio,
                language = dto.preferredLanguage
        )
    }

    fun mapSubscriptionDtoToDomain(dto: SubscriptionDto): Subscription {
        return Subscription(
                id = dto.id.toString(),
                userId = dto.userId.toString(),
                plan =
                        when (dto.plan.uppercase()) {
                            "FREE" -> SubscriptionPlan.FREE
                            "BASIC" -> SubscriptionPlan.BASIC
                            "PREMIUM" -> SubscriptionPlan.PREMIUM
                            "LIFETIME" -> SubscriptionPlan.LIFETIME
                            else -> SubscriptionPlan.FREE
                        },
                status =
                        when (dto.status.uppercase()) {
                            "ACTIVE" -> SubscriptionStatus.ACTIVE
                            "EXPIRED" -> SubscriptionStatus.EXPIRED
                            "CANCELLED" -> SubscriptionStatus.CANCELLED
                            "TRIAL" -> SubscriptionStatus.TRIAL
                            "SUSPENDED" -> SubscriptionStatus.SUSPENDED
                            else -> SubscriptionStatus.EXPIRED
                        },
                startDate = dto.startDate,
                endDate = dto.endDate
        )
    }
}
