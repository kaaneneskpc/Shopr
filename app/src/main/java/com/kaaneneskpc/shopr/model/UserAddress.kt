package com.kaaneneskpc.shopr.model

import android.os.Parcelable
import com.kaaneneskpc.domain.model.AddressDomainModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserAddress(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
) : Parcelable {
    override fun toString(): String {
        return "$addressLine, $city, $state, $postalCode, $country"
    }

    fun toAddressDataModel() = AddressDomainModel(
        addressLine,
        city,
        state,
        postalCode,
        country
    )
}

// Extension function to convert AddressDomainModel to UserAddress
fun AddressDomainModel.toUserAddress() = UserAddress(
    addressLine = this.addressLine,
    city = this.city,
    state = this.state,
    postalCode = this.postalCode,
    country = this.country
)