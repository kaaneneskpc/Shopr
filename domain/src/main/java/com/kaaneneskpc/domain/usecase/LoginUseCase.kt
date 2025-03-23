package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String) = userRepository.login(email, password)
}