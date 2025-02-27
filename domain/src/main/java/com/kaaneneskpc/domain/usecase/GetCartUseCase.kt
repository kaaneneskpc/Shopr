package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.CartRepository

class GetCartUseCase(private val cartRepository: CartRepository) {
    suspend fun execute() = cartRepository.getCart()
}