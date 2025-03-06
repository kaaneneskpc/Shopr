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
    val profileImage: String?, // Profil resmi URL'si
    val defaultAddress: AddressDomainModel?, // Varsayılan adres
    val createdAt: String, // Hesap oluşturma tarihi
    val lastLogin: String // Son giriş tarihi
) : Parcelable 