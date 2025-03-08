package com.kaaneneskpc.data.repository

import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.repository.ProfileRepository
import java.util.Date

class ProfileRepositoryImpl : ProfileRepository {
    
    private val mockUserProfile = UserProfile(
        userId = 1,
        name = "Kaan Enes",
        email = "kaan@example.com",
        phoneNumber = "+90 555 123 4567",
        profileImage = null,
        defaultAddress = AddressDomainModel(
            addressLine = "Atatürk Caddesi No:123",
            city = "İstanbul",
            state = "Kadıköy",
            postalCode = "34000",
            country = "Türkiye"
        ),
        createdAt = Date().toString(),
        lastLogin = Date().toString()
    )
    
    override suspend fun getUserProfile(userId: Int): ResultWrapper<UserProfile> {
        return ResultWrapper.Success(mockUserProfile)
    }

    override suspend fun updateUserProfile(userProfile: UserProfile): ResultWrapper<UserProfile> {
        return ResultWrapper.Success(userProfile)
    }

    override suspend fun addAddress(userId: Int, address: AddressDomainModel): ResultWrapper<AddressDomainModel> {
        return ResultWrapper.Success(address)
    }

    override suspend fun updateAddress(userId: Int, addressId: Int, address: AddressDomainModel): ResultWrapper<AddressDomainModel> {
        return ResultWrapper.Success(address)
    }

    override suspend fun deleteAddress(userId: Int, addressId: Int): ResultWrapper<Boolean> {
        return ResultWrapper.Success(true)
    }

    override suspend fun updatePassword(userId: Int, oldPassword: String, newPassword: String): ResultWrapper<Boolean> {
        return ResultWrapper.Success(true)
    }
} 