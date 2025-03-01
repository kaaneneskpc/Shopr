package com.kaaneneskpc.data.model.response.product

import com.kaaneneskpc.domain.model.ProductListModel
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val data: List<DataProductModel>,
    val msg: String
) {
    fun toProductList() = ProductListModel(
        products = data.map { it.toProduct() },
        msg = msg
    )
}