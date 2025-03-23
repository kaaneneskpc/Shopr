package com.kaaneneskpc.shopr.navigation

import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.shopr.model.UiProductModel
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
object CartScreen

@Serializable
object OrdersScreen

@Serializable
object ProfileScreen

@Serializable
object CartSummaryScreen

@Serializable
object AllProductsScreen

@Serializable
object WishlistsScreen

@Serializable
data class ProductDetails(val product: UiProductModel)

@Serializable
data class UserAddressRoute(val userAddressWrapper: UserAddressRouteWrapper)

@Serializable
data class EditProfileRoute(val userProfile: UserProfile)

@Serializable
object PaymentScreen

@Serializable
object PaymentVerificationScreen

@Serializable
object PaymentResultScreen

