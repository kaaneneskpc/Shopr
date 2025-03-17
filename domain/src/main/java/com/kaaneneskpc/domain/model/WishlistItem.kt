package com.kaaneneskpc.domain.model

data class WishlistItem(
    val id: Int,
    val productId: Int,
    val dateAdded: String,
    val priceAtTimeOfAdding: Double,
    val currentPrice: Double,
    val isInStock: Boolean,
    val notifyOnPriceChange: Boolean,
    val productTitle: String = "",
    val productImageUrl: String? = null
) 