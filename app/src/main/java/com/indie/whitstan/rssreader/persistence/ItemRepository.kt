package com.indie.whitstan.rssreader.persistence

import androidx.lifecycle.MutableLiveData

import org.koin.java.KoinJavaComponent

import retrofit2.Callback
import retrofit2.Response

import com.indie.whitstan.rssreader.RssReader
import com.indie.whitstan.rssreader.model.network.RSSObject
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle
import com.indie.whitstan.rssreader.model.persistence.LocalArticle
import com.indie.whitstan.rssreader.network.RetrofitClient
import com.indie.whitstan.rssreader.util.Converters

class ItemRepository {
    private val retrofitClient : RetrofitClient by KoinJavaComponent.inject(RetrofitClient::class.java)
    private val itemDao: ItemDao = ItemsDatabase.getInstance(RssReader.application)!!.itemDao()
    private var articlesLocalData = ArrayList<LocalArticle>()
    private var favoriteArticlesLocalData = ArrayList<FavoriteArticle>()

    suspend fun updateArticle(localArticle: LocalArticle): MutableLiveData<List<LocalArticle>> {
        itemDao.updateArticle(localArticle)
        val result : MutableLiveData<List<LocalArticle>> = MutableLiveData(listOf())
        val index = findElementInList(localArticle, articlesLocalData)
        articlesLocalData[index].setFavorite(localArticle.isFavorite())
        result.value = articlesLocalData
        return result
    }

    private fun findElementInList(localArticle : LocalArticle, list : ArrayList<LocalArticle>) : Int{
        list.forEachIndexed { index, element ->
            if (element == localArticle){
                return index
            }
        }
        return -1
    }

    fun fetchRssData(callback : Callback<RSSObject?>){
        retrofitClient.createRSSApi().getRSSObject?.enqueue(callback)
    }

    fun handleRssDataResponse(response : Response<RSSObject?>) : MutableLiveData<List<LocalArticle>>{
        val result : MutableLiveData<List<LocalArticle>> = MutableLiveData(listOf())
        response.body()!!.RSSItems.let {
            result.value = Converters.convertRssItemsToArticles(it)
        }
        return result
    }

    suspend fun loadArticlesFromDb() : List<LocalArticle>{
        val result = itemDao.getArticles()
        articlesLocalData = result as ArrayList
        return result
    }

    suspend fun loadFavoriteArticlesFromDb() : List<FavoriteArticle>{
        val result = itemDao.getFavoriteArticles()
        favoriteArticlesLocalData = result as ArrayList
        return result
    }

    suspend fun insertFavoriteArticle(favoriteArticle: FavoriteArticle) : MutableLiveData<List<FavoriteArticle>>{
        val result : MutableLiveData<List<FavoriteArticle>> = MutableLiveData(listOf())
        itemDao.insertFavoriteArticle(favoriteArticle)
        favoriteArticlesLocalData = itemDao.getFavoriteArticles() as ArrayList<FavoriteArticle>
        result.value = favoriteArticlesLocalData
        return result
    }

    suspend fun deleteFavoriteArticle(favoriteArticle: FavoriteArticle) : MutableLiveData<List<FavoriteArticle>>{
        val result : MutableLiveData<List<FavoriteArticle>> = MutableLiveData(listOf())
        itemDao.deleteFavoriteArticle(favoriteArticle)
        favoriteArticlesLocalData = itemDao.getFavoriteArticles() as ArrayList<FavoriteArticle>
        result.value = favoriteArticlesLocalData
        return result
    }

    suspend fun replaceArticles(localArticles : List<LocalArticle>) : MutableLiveData<List<LocalArticle>>{
        itemDao.replaceArticles(localArticles)
        val result : MutableLiveData<List<LocalArticle>> = MutableLiveData(listOf())
        articlesLocalData.clear()
        articlesLocalData.addAll(localArticles)
        result.value = articlesLocalData
        return result
    }
}