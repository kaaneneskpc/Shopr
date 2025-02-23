package com.kaaneneskpc.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val data: List<String>,
    val msg: String
)