package com.kaaneneskpc.shopr.di

import com.kaaneneskpc.shopr.ui.feature.wishlist.WishlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val wishlistModule = module {
    viewModel {
        WishlistViewModel()
    }
} 