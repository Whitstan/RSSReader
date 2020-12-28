package com.indie.whitstan.rssreader.viewmodel

import androidx.lifecycle.*

import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle
import com.indie.whitstan.rssreader.persistence.ItemRepository
import org.koin.java.KoinJavaComponent.inject

class ItemViewModel : ViewModel(){
    private val articlesLiveData = MutableLiveData<List<Article>>()
    private val favoritesLiveData = MutableLiveData<List<FavoriteArticle>>()

    val articlesMediatorData = MediatorLiveData<List<Article>>()
    val favoritesMediatorData = MediatorLiveData<List<FavoriteArticle>>()

    private val repository : ItemRepository by inject(ItemRepository::class.java)

    init {
        articlesMediatorData.addSource(articlesLiveData) {
            articlesMediatorData.value = it
        }

        favoritesMediatorData.addSource(favoritesLiveData) {
            favoritesMediatorData.value = it
        }
    }

    fun loadArticlesFromDb() {
        if (articlesLiveData.value.isNullOrEmpty()) {
            repository.loadArticlesFromDb(this@ItemViewModel)
        }
    }

    fun loadFavoritesFromDb(){
        if (favoritesLiveData.value.isNullOrEmpty()) {
            repository.loadFavoritesFromDb(this@ItemViewModel)
        }
    }

}