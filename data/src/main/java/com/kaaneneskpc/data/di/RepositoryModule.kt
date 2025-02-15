package com.kaaneneskpc.data.di

import com.kaaneneskpc.data.repository.ProductRepositoryImpl
import com.kaaneneskpc.domain.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}