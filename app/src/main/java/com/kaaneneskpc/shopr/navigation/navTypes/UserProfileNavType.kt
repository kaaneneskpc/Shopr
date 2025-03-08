package com.kaaneneskpc.shopr.navigation.navTypes

import android.os.Bundle
import androidx.navigation.NavType
import com.kaaneneskpc.domain.model.UserProfile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val userProfileNavType = object : NavType<UserProfile>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UserProfile? {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, UserProfile::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): UserProfile {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: UserProfile): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: UserProfile) {
        bundle.putParcelable(key, value)
    }
} 