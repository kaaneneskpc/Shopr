package com.kaaneneskpc.data.repository

import com.kaaneneskpc.domain.model.ProductListModel
import com.kaaneneskpc.domain.network.NetworkService
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.repository.ProductRepository

class ProductRepositoryImpl(private val networkService: NetworkService) : ProductRepository {
    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        return networkService.getProducts(category)
    }
}