package com.indie.whitstan.rssreader.interfaces

import com.indie.whitstan.rssreader.model.RSSObject
import retrofit2.Call
import retrofit2.http.GET

interface RSSAPI {
    @get:GET("24ora/rss")
    val getRSSObject: Call<RSSObject?>?
}