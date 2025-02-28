package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.CartItemModel
import com.kaaneneskpc.domain.repository.CartRepository

class UpdateQuantityUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(cartItemModel: CartItemModel) = cartRepository.updateQuantity(cartItemModel)
}