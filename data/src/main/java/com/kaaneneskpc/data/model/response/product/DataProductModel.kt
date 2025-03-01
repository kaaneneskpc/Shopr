package com.kaaneneskpc.data.model.response.product

import com.kaaneneskpc.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
class DataProductModel(
    val categoryId: Int,
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String
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