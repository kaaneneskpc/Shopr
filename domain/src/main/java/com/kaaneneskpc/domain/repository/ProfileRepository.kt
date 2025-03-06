package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.domain.network.ResultWrapper

interface ProfileRepository {
    suspend fun getUserProfile(userId: Int): ResultWrapper<UserProfile>
    suspend fun updateUserProfile(userProfile: UserProfile): ResultWrapper<UserProfile>
    suspend fun addAddress(userId: Int, address: AddressDomainModel): ResultWrapper<AddressDomainModel>
    suspend fun updateAddress(userId: Int, addressId: Int, address: AddressDomainModel): ResultWrapper<AddressDomainModel>
    suspend fun deleteAddress(userId: Int, addressId: Int): ResultWrapper<Boolean>
    suspend fun updatePassword(userId: Int, oldPassword: String, newPassword: String): ResultWrapper<Boolean>
} 