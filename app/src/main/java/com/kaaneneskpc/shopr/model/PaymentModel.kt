package com.kaaneneskpc.shopr.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class PaymentModel(
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val cardHolderName: String = ""
)

@Parcelize
@Serializable
data class PaymentResult(
    val isSuccess: Boolean,
    val message: String,
    val transactionId: String? = null
) : Parcelable 