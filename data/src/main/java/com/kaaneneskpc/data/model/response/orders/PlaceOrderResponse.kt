package com.kaaneneskpc.data.model.response.orders

import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderResponse(
    val data: OrderD,
    val msg: String
)