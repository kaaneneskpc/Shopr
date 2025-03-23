package com.kaaneneskpc.data.model.response.user

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthResponse(
    val data: UserResponse,
    val msg: String
)