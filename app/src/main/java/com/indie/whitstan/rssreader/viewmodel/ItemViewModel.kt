package com.indie.whitstan.rssreader.viewmodel

import androidx.lifecycle.*

import de.greenrobot.event.EventBus

import kotlinx.coroutines.launch

import org.koin.core.KoinComponent
import org.koin.java.KoinJavaComponent.inject
import org.koin.core.inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.indie.whitstan.rssreader.event.EventArticlesRefreshed
import com.indie.whitstan.rssreader.model.network.RSSObject
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.persistence.ItemRepository

class ItemViewModel : ViewModel(), KoinComponent{

    private var articlesLiveData = MutableLiveData<List<Article>>()
    private var favoriteArticlesLiveData = MutableLiveData<List<Article>>()

    private val repository : ItemRepository by inject(ItemRepository::class.java)
    private val eventBus : EventBus by inject()

    fun getArticles():MutableLiveData<List<Article>>{
        return articlesLiveData
    }

    fun updateArticle(article: Article) {
        viewModelScope.launch {
            articlesLiveData.value = repository.updateArticle(article).value
            favoriteArticlesLiveData.value = articlesLiveData.value?.filter { it.isFavorite()}
        }
    }

    fun refreshArticlesData() {
        repository.fetchRssData(object : Callback<RSSObject?> {
            override fun onResponse(call: Call<RSSObject?>, response: Response<RSSObject?>) {
                viewModelScope.launch {
                    val responseValue = repository.handleRssDataResponse(response).value
                    if (!responseValue.isNullOrEmpty()){
                        articlesLiveData.value = repository.replaceArticles(responseValue).value
                    }
                    eventBus.post(EventArticlesRefreshed())
                }
            }
            override fun onFailure(call: Call<RSSObject?>, error: Throwable) {
                error.printStackTrace()
                eventBus.post(EventArticlesRefreshed())
            }
        })
    }

    fun loadArticlesFromDb() {
        if (articlesLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                articlesLiveData.value = repository.loadArticlesFromDb()
            }
        }
    }

    fun getFavoriteArticles(): MutableLiveData<List<Article>>{
        favoriteArticlesLiveData.value = articlesLiveData.value?.filter { it.isFavorite()}
        return favoriteArticlesLiveData
    }

}