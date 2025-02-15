package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.Product
import com.kaaneneskpc.domain.model.ProductListModel
import com.kaaneneskpc.domain.network.ResultWrapper

interface ProductRepository {
    suspend fun getProducts(): ResultWrapper<List<Product>>
}