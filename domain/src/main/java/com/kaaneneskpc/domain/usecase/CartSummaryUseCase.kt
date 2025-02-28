package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.CartRepository

class CartSummaryUseCase (private val repository: CartRepository) {
    suspend fun execute(userId: Int) = repository.getCartSummary(userId)
}