package com.kaaneneskpc.domain.di

import com.kaaneneskpc.domain.usecase.AddToCartUseCase
import com.kaaneneskpc.domain.usecase.GetCartUseCase
import com.kaaneneskpc.domain.usecase.GetCategoriesUseCase
import com.kaaneneskpc.domain.usecase.GetProductsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProductsUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { AddToCartUseCase(get()) }
    factory { GetCartUseCase(get()) }
}