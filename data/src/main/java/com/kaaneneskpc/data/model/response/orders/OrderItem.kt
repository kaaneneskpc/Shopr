package com.kaaneneskpc.data.model.response.orders

import com.kaaneneskpc.domain.model.OrderProductItem
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: Int,
    val orderId: Int,
    val price: Double,
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val userId: Int
) {
    fun toDomainResponse(): OrderProductItem {
        return OrderProductItem(
            id = id,
            orderId = orderId,
            price = price,
            productId = productId,
            productName = productName,
            quantity = quantity,
            userId = userId
        )
    }
}