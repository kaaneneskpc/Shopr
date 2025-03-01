package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.OrderRepository

class OrderListUseCase(private val repository: OrderRepository) {
    suspend fun execute() = repository.getOrderList()
}