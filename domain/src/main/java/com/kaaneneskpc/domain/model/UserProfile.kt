package com.kaaneneskpc.domain.model

data class UserProfile(
    val userId: Int,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profileImage: String?, // Profil resmi URL'si
    val defaultAddress: AddressDomainModel?, // Varsayılan adres
    val createdAt: String, // Hesap oluşturma tarihi
    val lastLogin: String // Son giriş tarihi
) 