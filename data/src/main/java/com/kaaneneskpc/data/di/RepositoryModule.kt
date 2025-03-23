package com.kaaneneskpc.data.di

import com.kaaneneskpc.data.repository.CartRepositoryImpl
import com.kaaneneskpc.data.repository.CategoryRepositoryImpl
import com.kaaneneskpc.data.repository.OrderRepositoryImpl
import com.kaaneneskpc.data.repository.ProductRepositoryImpl
import com.kaaneneskpc.data.repository.ProfileRepositoryImpl
import com.kaaneneskpc.data.repository.UserRepositoryImpl
import com.kaaneneskpc.domain.repository.CartRepository
import com.kaaneneskpc.domain.repository.CategoryRepository
import com.kaaneneskpc.domain.repository.OrderRepository
import com.kaaneneskpc.domain.repository.ProductRepository
import com.kaaneneskpc.domain.repository.ProfileRepository
import com.kaaneneskpc.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }
}