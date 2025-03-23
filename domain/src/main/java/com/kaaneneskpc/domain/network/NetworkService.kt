package com.kaaneneskpc.domain.network

import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.model.CartItemModel
import com.kaaneneskpc.domain.model.CartModel
import com.kaaneneskpc.domain.model.CartSummary
import com.kaaneneskpc.domain.model.CategoryListModel
import com.kaaneneskpc.domain.model.OrdersListModel
import com.kaaneneskpc.domain.model.ProductListModel
import com.kaaneneskpc.domain.model.UserDomainModel
import com.kaaneneskpc.domain.model.request.AddCartRequestModel

interface NetworkService {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
    suspend fun getCategories(): ResultWrapper<CategoryListModel>
    suspend fun addProductToCart(request: AddCartRequestModel): ResultWrapper<CartModel>
    suspend fun getCart(): ResultWrapper<CartModel>
    suspend fun updateQuantity(cartItemModel: CartItemModel): ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId: Int, userId: Int): ResultWrapper<CartModel>
    suspend fun getCartSummary(userId: Int): ResultWrapper<CartSummary>
    suspend fun placeOrder(address: AddressDomainModel, userId: Int): ResultWrapper<Long>
    suspend fun getOrderList(): ResultWrapper<OrdersListModel>
    suspend fun login(email: String, password: String): ResultWrapper<UserDomainModel>
    suspend fun register(email: String, password: String, name: String): ResultWrapper<UserDomainModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
}