package com.indie.whitstan.rssreader.network

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import com.indie.whitstan.rssreader.interfaces.RSSAPI
import org.koin.core.KoinComponent

interface IRetrofitClient{
    fun createRSSApi() : RSSAPI
}

class RetrofitClient : IRetrofitClient{
    override fun createRSSApi(): RSSAPI {
        val retrofit = Retrofit.Builder()
                .baseUrl(RSS_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(RSSAPI::class.java)
    }

    companion object {
        const val RSS_BASE_URL = "https://index.hu/"
    }
}