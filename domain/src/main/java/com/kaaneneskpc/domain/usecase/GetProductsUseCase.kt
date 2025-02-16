package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend fun execute(category: String?) = repository.getProducts(category)
}