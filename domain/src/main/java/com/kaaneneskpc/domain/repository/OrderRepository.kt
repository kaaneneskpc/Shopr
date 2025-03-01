package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.model.OrdersListModel
import com.kaaneneskpc.domain.network.ResultWrapper

interface OrderRepository {
    suspend fun placeOrder(addressDomainModel: AddressDomainModel): ResultWrapper<Long>
    suspend fun getOrderList(): ResultWrapper<OrdersListModel>
}