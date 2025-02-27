package com.kaaneneskpc.shopr.di

import com.kaaneneskpc.shopr.ui.feature.cart.CartViewModel
import com.kaaneneskpc.shopr.ui.feature.home.HomeViewModel
import com.kaaneneskpc.shopr.ui.feature.productDetails.ProductDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProductDetailsViewModel(get()) }
    viewModel { CartViewModel(get()) }
}