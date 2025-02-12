package com.kaaneneskpc.data.model

import com.kaaneneskpc.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
class DataProductModel(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val title: String
) {

    fun toProduct() = Product(
        id = id,
        title = title,
        price = price,
        categoryId = categoryId,
        description = description,
        image = image
    )
}