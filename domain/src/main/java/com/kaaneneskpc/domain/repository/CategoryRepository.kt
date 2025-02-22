package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories(): ResultWrapper<List<String>>
}