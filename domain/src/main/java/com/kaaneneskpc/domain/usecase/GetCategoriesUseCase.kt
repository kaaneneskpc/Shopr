package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.CategoryRepository

class GetCategoriesUseCase(private val repository: CategoryRepository) {
    suspend fun execute() = repository.getCategories()
}