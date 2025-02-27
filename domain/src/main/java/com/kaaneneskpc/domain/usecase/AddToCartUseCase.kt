package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.request.AddCartRequestModel
import com.kaaneneskpc.domain.repository.CartRepository

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(request: AddCartRequestModel) = cartRepository.addProductToCart(request)
}