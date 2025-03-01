package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.AddressDomainModel
import com.kaaneneskpc.domain.repository.OrderRepository

class PlaceOrderUseCase(private val orderRepository: OrderRepository) {
    suspend fun execute(addressDomainModel: AddressDomainModel) =
        orderRepository.placeOrder(addressDomainModel)
}