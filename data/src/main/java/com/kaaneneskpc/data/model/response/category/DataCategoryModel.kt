package com.kaaneneskpc.data.model.response.category

import com.kaaneneskpc.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
class DataCategoryModel(
    val id: Int,
    val image: String,
    val title: String
) {
    fun toCategory() = Category(
        id = id,
        image = image,
        title = title
    )
}