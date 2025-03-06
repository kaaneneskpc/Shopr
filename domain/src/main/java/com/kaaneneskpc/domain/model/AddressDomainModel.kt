package com.kaaneneskpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AddressDomainModel(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
) : Parcelable