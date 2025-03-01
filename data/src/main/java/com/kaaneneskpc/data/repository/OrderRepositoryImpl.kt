package com.kaaneneskpc.data.repository

import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.model.OrdersListModel
import com.kaaneneskpc.domain.network.NetworkService
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.repository.OrderRepository

class OrderRepositoryImpl(private val networkService: NetworkService) : OrderRepository {
    override suspend fun placeOrder(addressDomainModel: AddressDomainModel): ResultWrapper<Long> {
        return networkService.placeOrder(addressDomainModel, 1)
    }

    override suspend fun getOrderList(): ResultWrapper<OrdersListModel> {
        return networkService.getOrderList()
    }
}