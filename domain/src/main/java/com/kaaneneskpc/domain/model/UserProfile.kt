package com.kaaneneskpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserProfile(
    val userId: Int,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profileImage: String?,
    val defaultAddress: AddressDomainModel?,
    val createdAt: String,
    val lastLogin: String
) : Parcelable 