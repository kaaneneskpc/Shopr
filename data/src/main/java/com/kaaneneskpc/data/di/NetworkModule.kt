package com.kaaneneskpc.data.di

import android.util.Log
import com.kaaneneskpc.data.network.NetworkServiceImpl
import com.kaaneneskpc.domain.network.NetworkService
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("BackEndHandler", message)
                    }
                }
            }
        }
    }

    single<NetworkService> {
        NetworkServiceImpl(get())
    }
}