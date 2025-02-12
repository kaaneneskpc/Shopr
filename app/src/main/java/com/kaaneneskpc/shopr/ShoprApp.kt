package com.kaaneneskpc.shopr

import android.app.Application
import com.kaaneneskpc.data.di.dataModule
import com.kaaneneskpc.domain.di.domainModule
import com.kaaneneskpc.shopr.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShoprApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShoprApp)
            modules(listOf(
                appModule,
                dataModule,
                domainModule
            ))
        }
    }
}