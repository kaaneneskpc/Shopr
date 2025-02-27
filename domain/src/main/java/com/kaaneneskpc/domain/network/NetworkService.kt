package com.kaaneneskpc.domain.network

import com.kaaneneskpc.domain.model.CartModel
import com.kaaneneskpc.domain.model.CategoryListModel
import com.kaaneneskpc.domain.model.ProductListModel
import com.kaaneneskpc.domain.model.request.AddCartRequestModel

interface NetworkService {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
    suspend fun getCategories(): ResultWrapper<CategoryListModel>
    suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>
    suspend fun getCart(): ResultWrapper<CartModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
}