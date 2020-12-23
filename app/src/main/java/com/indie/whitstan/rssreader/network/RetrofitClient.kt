package com.indie.whitstan.rssreader.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import com.indie.whitstan.rssreader.interfaces.RSSAPI
import com.indie.whitstan.rssreader.model.RSSObject

class RetrofitClient {
    fun createCall(): Call<RSSObject?>? {
        val retrofit = Retrofit.Builder()
                .baseUrl(RSS_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val rssAPI = retrofit.create(RSSAPI::class.java)
        return rssAPI.rssObjects
    }

    companion object {
        const val RSS_BASE_URL = "https://index.hu/"
    }
}