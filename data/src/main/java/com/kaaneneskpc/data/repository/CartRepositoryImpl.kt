package com.kaaneneskpc.data.repository

import com.kaaneneskpc.domain.model.CartModel
import com.kaaneneskpc.domain.model.request.AddCartRequestModel
import com.kaaneneskpc.domain.network.NetworkService
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.repository.CartRepository

class CartRepositoryImpl(private val networkService: NetworkService) : CartRepository {
    override suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel> {
        return networkService.addProductToCart(request)
    }

    override suspend fun getCart(): ResultWrapper<CartModel> {
        return networkService.getCart()
    }
}