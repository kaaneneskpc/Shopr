package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.CategoryListModel
import com.kaaneneskpc.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories(): ResultWrapper<CategoryListModel>
}