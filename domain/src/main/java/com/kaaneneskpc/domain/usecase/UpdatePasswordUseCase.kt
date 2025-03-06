package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.ProfileRepository

class UpdatePasswordUseCase(private val repository: ProfileRepository) {
    suspend fun execute(userId: Int, oldPassword: String, newPassword: String) = 
        repository.updatePassword(userId, oldPassword, newPassword)
} 