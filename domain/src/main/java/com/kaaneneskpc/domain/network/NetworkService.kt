package com.kaaneneskpc.domain.network

import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.domain.model.ProductListModel

interface NetworkService {
    suspend fun getProducts(): ResultWrapper<List<Product>>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
}