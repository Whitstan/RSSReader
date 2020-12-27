package com.indie.whitstan.rssreader

import org.koin.dsl.module

import org.koin.android.viewmodel.dsl.viewModel

import com.indie.whitstan.rssreader.network.IRetrofitClient
import com.indie.whitstan.rssreader.network.RetrofitClient
import com.indie.whitstan.rssreader.persistence.IItemRepository
import com.indie.whitstan.rssreader.persistence.ItemRepository
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

object AppModule {
    fun getModules() = module {

        // ViewModel
        viewModel { ItemViewModel() }

        // Persistence
        single { ItemRepository() }

        // Network
        single { RetrofitClient() }
    }
}