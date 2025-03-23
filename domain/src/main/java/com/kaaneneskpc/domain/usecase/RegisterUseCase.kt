package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String, name: String) = userRepository.register(email, password, name)
}