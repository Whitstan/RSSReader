package com.indie.whitstan.rssreader

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class RssReader : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RssReader)
            modules(AppModule.getModules())
        }
    }
}