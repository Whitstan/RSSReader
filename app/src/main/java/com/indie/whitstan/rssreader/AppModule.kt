package com.indie.whitstan.rssreader

import com.indie.whitstan.rssreader.network.IRetrofitClient
import com.indie.whitstan.rssreader.network.RetrofitClient
import org.koin.core.KoinComponent
import org.koin.dsl.module

object AppModule {
    fun getModules() = module {

        // Network
        single<IRetrofitClient> { RetrofitClient() }
    }
}