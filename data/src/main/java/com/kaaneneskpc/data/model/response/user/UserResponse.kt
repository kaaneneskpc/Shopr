package com.kaaneneskpc.data.model.response.user

import com.kaaneneskpc.domain.model.UserDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int?,
    val username: String,
    val email: String,
    val name: String
) {
    fun toDomainModel(): UserDomainModel {
        return UserDomainModel(
            id = id,
            username = username,
            email = email,
            name = name
        )
    }
}