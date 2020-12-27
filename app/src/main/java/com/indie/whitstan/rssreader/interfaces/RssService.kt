package com.indie.whitstan.rssreader.interfaces

import com.indie.whitstan.rssreader.model.network.RSSObject
import retrofit2.Call
import retrofit2.http.GET

interface RssService {
    //@get:GET("24ora/rss")
    @get:GET("sample-feed.xml")
    val getRSSObject: Call<RSSObject?>?
}