package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.UserDomainModel
import com.kaaneneskpc.domain.network.ResultWrapper

interface UserRepository {
    suspend fun login(email: String, password: String): ResultWrapper<UserDomainModel>
    suspend fun register(email: String, password: String, name: String): ResultWrapper<UserDomainModel>
}