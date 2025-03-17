package com.kaaneneskpc.domain.model

data class Wishlist(
    val id: Int,
    val userId: Int,
    val name: String,
    val isPublic: Boolean,
    val shareableLink: String?,
    val items: List<WishlistItem>,
    val createdAt: String
) 