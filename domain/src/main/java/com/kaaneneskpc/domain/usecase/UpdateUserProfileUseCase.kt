package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.domain.repository.ProfileRepository

class UpdateUserProfileUseCase(private val repository: ProfileRepository) {
    suspend fun execute(userProfile: UserProfile) = repository.updateUserProfile(userProfile)
} 