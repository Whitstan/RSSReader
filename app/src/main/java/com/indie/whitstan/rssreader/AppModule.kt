package com.indie.whitstan.rssreader

import org.koin.dsl.module

import org.koin.android.viewmodel.dsl.viewModel

import com.indie.whitstan.rssreader.network.RetrofitClient
import com.indie.whitstan.rssreader.persistence.ItemRepository
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel
import de.greenrobot.event.EventBus

object AppModule {
    fun getModules() = module {

        // ViewModel
        viewModel { ItemViewModel() }

        // Persistence
        single { ItemRepository() }

        // Network
        single { RetrofitClient() }

        // Utility
        single<EventBus>(override = true) { EventBus.getDefault() }
    }
}