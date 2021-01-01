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
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle
import com.indie.whitstan.rssreader.model.persistence.LocalArticle
import com.indie.whitstan.rssreader.persistence.ItemRepository
import com.indie.whitstan.rssreader.util.Comparison.Companion.crossEquals

class ItemViewModel : ViewModel(), KoinComponent{

    private var articlesLiveData = MutableLiveData<List<LocalArticle>>()
    private var favoriteArticlesLiveData = MutableLiveData<List<FavoriteArticle>>()

    private val repository : ItemRepository by inject(ItemRepository::class.java)
    private val eventBus : EventBus by inject()

    fun loadDataFromDb(){
        loadArticlesFromDb()
        loadFavoriteArticlesFromDb()
    }

    // LocalArticles

    private fun loadArticlesFromDb() {
        if (articlesLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                articlesLiveData.value = repository.loadArticlesFromDb()
            }
        }
    }

    fun getArticlesLiveData():MutableLiveData<List<LocalArticle>>{
        return articlesLiveData
    }

    fun updateArticle(localArticle: LocalArticle) {
        viewModelScope.launch {
            articlesLiveData.value = repository.updateArticle(localArticle).value
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

    // Favorites

    private fun loadFavoriteArticlesFromDb(){
        if (favoriteArticlesLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                favoriteArticlesLiveData.value = repository.loadFavoriteArticlesFromDb()
            }
        }
    }

    fun getFavoriteArticlesLiveData(): MutableLiveData<List<FavoriteArticle>> {
        return favoriteArticlesLiveData
    }

    fun getLocalArticleByFavoriteArticle(favoriteArticle: FavoriteArticle) : LocalArticle?{
        var result : LocalArticle? = null
        val index = findLocalArticleIndexByFavoriteArticle(favoriteArticle)
        if (index != -1){
            result = (articlesLiveData.value as ArrayList<LocalArticle>)[index]
        }
        return result
    }

    fun getFavoriteArticleByLocalArticle(localArticle: LocalArticle) : FavoriteArticle{
        val index = findFavoriteArticleIndexByLocalArticle(localArticle)
        return (favoriteArticlesLiveData.value as ArrayList<FavoriteArticle>)[index]
    }

    private fun findLocalArticleIndexByFavoriteArticle(favoriteArticle : FavoriteArticle) : Int{
        (articlesLiveData.value as ArrayList<LocalArticle>).forEachIndexed { index, localArticle ->
            if (crossEquals(localArticle, favoriteArticle)){
                return index
            }
        }
        return -1
    }

    private fun findFavoriteArticleIndexByLocalArticle(localArticle: LocalArticle) : Int{
        (favoriteArticlesLiveData.value as ArrayList<FavoriteArticle>).forEachIndexed { index, favoriteArticle ->
            if (crossEquals(localArticle, favoriteArticle)){
                return index
            }
        }
        return -1
    }

    fun insertFavoriteArticle(favoriteArticle: FavoriteArticle){
        viewModelScope.launch {
            favoriteArticlesLiveData.value = repository.insertFavoriteArticle(favoriteArticle).value
        }
    }

    fun deleteFavoriteArticle(favoriteArticle: FavoriteArticle){
        viewModelScope.launch {
            favoriteArticlesLiveData.value = repository.deleteFavoriteArticle(favoriteArticle).value
        }
    }

}