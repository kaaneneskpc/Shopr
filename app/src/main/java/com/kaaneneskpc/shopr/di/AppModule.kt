package com.kaaneneskpc.shopr.di

import org.koin.dsl.module

val appModule = module {
    includes(viewModelModule)
}