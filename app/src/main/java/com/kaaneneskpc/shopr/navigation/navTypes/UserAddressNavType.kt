package com.kaaneneskpc.shopr.navigation.navTypes

import android.os.Build
import com.kaaneneskpc.shopr.navigation.UserAddressRouteWrapper
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val userAddressNavType = object : NavType<UserAddressRouteWrapper>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UserAddressRouteWrapper? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            return bundle.getParcelable(key, UserAddressRouteWrapper::class.java)
        return bundle.getParcelable(key) as? UserAddressRouteWrapper
    }

    override fun parseValue(value: String): UserAddressRouteWrapper {
        val item = Json.decodeFromString<UserAddressRouteWrapper>(value)
        return item
    }

    override fun serializeAsValue(value: UserAddressRouteWrapper): String {
        return Json.encodeToString(
            value
        )
    }

    override fun put(bundle: Bundle, key: String, value: UserAddressRouteWrapper) {
        bundle.putParcelable(key, value)
    }
}
