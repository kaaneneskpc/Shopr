package com.kaaneneskpc.data.repository

import com.kaaneneskpc.domain.model.CategoryListModel
import com.kaaneneskpc.domain.network.NetworkService
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.repository.CategoryRepository

class CategoryRepositoryImpl(private val networkService: NetworkService) : CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<CategoryListModel> {
        return networkService.getCategories()
    }
}