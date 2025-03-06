package com.kaaneneskpc.domain.di

import com.kaaneneskpc.domain.usecase.AddToCartUseCase
import com.kaaneneskpc.domain.usecase.CartSummaryUseCase
import com.kaaneneskpc.domain.usecase.DeleteProductUseCase
import com.kaaneneskpc.domain.usecase.GetCartUseCase
import com.kaaneneskpc.domain.usecase.GetCategoriesUseCase
import com.kaaneneskpc.domain.usecase.GetProductsUseCase
import com.kaaneneskpc.domain.usecase.GetUserProfileUseCase
import com.kaaneneskpc.domain.usecase.OrderListUseCase
import com.kaaneneskpc.domain.usecase.PlaceOrderUseCase
import com.kaaneneskpc.domain.usecase.UpdatePasswordUseCase
import com.kaaneneskpc.domain.usecase.UpdateQuantityUseCase
import com.kaaneneskpc.domain.usecase.UpdateUserProfileUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProductsUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { AddToCartUseCase(get()) }
    factory { GetCartUseCase(get()) }
    factory { UpdateQuantityUseCase(get()) }
    factory { DeleteProductUseCase(get()) }
    factory { CartSummaryUseCase(get()) }
    factory { PlaceOrderUseCase(get()) }
    factory { OrderListUseCase(get()) }
    
    // Profile related use cases
    factory { GetUserProfileUseCase(get()) }
    factory { UpdateUserProfileUseCase(get()) }
    factory { UpdatePasswordUseCase(get()) }
}