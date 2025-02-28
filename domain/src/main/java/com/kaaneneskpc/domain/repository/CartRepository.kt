package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.CartItemModel
import com.kaaneneskpc.domain.model.CartModel
import com.kaaneneskpc.domain.model.CartSummary
import com.kaaneneskpc.domain.model.request.AddCartRequestModel
import com.kaaneneskpc.domain.network.ResultWrapper

interface CartRepository {
    suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>
    suspend fun getCart(): ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel): ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId: Int, userId: Int): ResultWrapper<CartModel>
    suspend fun getCartSummary(userId: Int): ResultWrapper<CartSummary>
}