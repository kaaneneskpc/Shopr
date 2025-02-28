package com.kaaneneskpc.shopr.navigation

import com.kaaneneskpc.shopr.model.UiProductModel
import com.kaaneneskpc.shopr.model.UserAddress
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object CartScreen

@Serializable
object ProfileScreen

@Serializable
object CartSummaryScreen

@Serializable
data class ProductDetails(val product: UiProductModel)

@Serializable
data class UserAddressRoute(val userAddressWrapper: UserAddressRouteWrapper)