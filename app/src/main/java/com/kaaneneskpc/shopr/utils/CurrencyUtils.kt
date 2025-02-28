package com.kaaneneskpc.shopr.utils

import java.text.NumberFormat
import java.util.Currency

object CurrencyUtils {

    fun formatPrice(price: Double, currency: String = "USD"): String {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(currency)
        return format.format(price)
    }
}