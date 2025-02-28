package com.kaaneneskpc.shopr.navigation

import android.os.Parcelable
import com.kaaneneskpc.shopr.model.UserAddress
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserAddressRouteWrapper(
    val userAddress: UserAddress?
) : Parcelable