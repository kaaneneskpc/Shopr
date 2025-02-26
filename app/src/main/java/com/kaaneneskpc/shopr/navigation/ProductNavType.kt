package com.kaaneneskpc.shopr.navigation

import android.os.Build
import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.kaaneneskpc.shopr.model.UiProductModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64

val productNavType = object : NavType<UiProductModel>(isNullableAllowed = false) {
    @Suppress("DEPRECATION")
    override fun get(bundle: Bundle, key: String): UiProductModel? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            return bundle.getParcelable(key, UiProductModel::class.java)
        return bundle.getParcelable(key) as? UiProductModel
    }

    override fun parseValue(value: String): UiProductModel {
        val item = Json.decodeFromString<UiProductModel>(value)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            item.copy(
                image = URLDecoder.decode(item.image, "UTF-8"),
                description = String(
                    Base64.getDecoder().decode(item.description.replace("_", "/"))
                ),
                title = String(Base64.getDecoder().decode(item.title.replace("_", "/")))
            )
        } else {
            item.copy(
                image = URLDecoder.decode(item.image, "UTF-8"),
                description = String(
                    android.util.Base64.decode(
                        item.description.replace("_", "/"),
                        android.util.Base64.DEFAULT
                    )
                ),
                title = String(
                    android.util.Base64.decode(
                        item.title.replace("_", "/"),
                        android.util.Base64.DEFAULT
                    )
                )
            )
        }
    }

    override fun serializeAsValue(value: UiProductModel): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Json.encodeToString(
                value.copy(
                    image = URLEncoder.encode(value.image, "UTF-8"),
                    description = String(
                        Base64.getEncoder().encode(value.description.toByteArray())
                    ).replace("/", "_"),
                    title = String(Base64.getEncoder().encode(value.title.toByteArray())).replace(
                        "/",
                        "_"
                    )
                )
            )
        } else {
            Json.encodeToString(
                value.copy(
                    image = URLEncoder.encode(value.image, "UTF-8"),
                    description = String(
                        android.util.Base64.encode(value.description.toByteArray(), android.util.Base64.DEFAULT)
                    ).replace("/", "_"),
                    title = String(
                        android.util.Base64.encode(value.title.toByteArray(), android.util.Base64.DEFAULT)
                    ).replace("/", "_")
                )
            )
        }
    }

    override fun put(bundle: Bundle, key: String, value: UiProductModel) {
        bundle.putParcelable(key, value)
    }

}