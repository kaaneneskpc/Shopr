package com.kaaneneskpc.data.repository

import com.kaaneneskpc.domain.network.NetworkService
import com.kaaneneskpc.domain.repository.UserRepository

class UserRepositoryImpl(private val networkService: NetworkService) : UserRepository {
    override suspend fun register(email: String, password: String, name: String) =
        networkService.register(email, password, name)

    override suspend fun login(email: String, password: String) =
        networkService.login(email, password)
}