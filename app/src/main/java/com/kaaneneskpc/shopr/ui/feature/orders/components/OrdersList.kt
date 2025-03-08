package com.kaaneneskpc.shopr.ui.feature.orders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kaaneneskpc.domain.model.OrdersData

@Composable
fun OrderList(orders: List<OrdersData>) {
    if (orders.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No Orders")
        }
    } else {
        LazyColumn {
            items(orders, key = { order -> order.id }) {
                OrderItem(order = it)
            }
        }
    }
}
