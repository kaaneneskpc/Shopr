package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.ProfileRepository

class GetUserProfileUseCase(private val repository: ProfileRepository) {
    suspend fun execute(userId: Int) = repository.getUserProfile(userId)
} 