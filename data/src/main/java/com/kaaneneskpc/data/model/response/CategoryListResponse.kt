package com.kaaneneskpc.data.model.response

import com.kaaneneskpc.data.model.DataCategoryModel
import com.kaaneneskpc.domain.model.CategoryListModel
import kotlinx.serialization.Serializable

@Serializable
data class CategoryListResponse(
    val data: List<DataCategoryModel>,
    val msg: String
) {
    fun toCategoryList() = CategoryListModel(
        category = data.map { it.toCategory() },
        msg = msg
    )
}