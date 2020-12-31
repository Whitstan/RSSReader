package com.indie.whitstan.rssreader.persistence

import androidx.lifecycle.MutableLiveData

import org.koin.java.KoinJavaComponent

import retrofit2.Callback
import retrofit2.Response

import com.indie.whitstan.rssreader.RssReader
import com.indie.whitstan.rssreader.model.network.RSSObject
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.network.RetrofitClient
import com.indie.whitstan.rssreader.util.Converters

class ItemRepository {
    private val retrofitClient : RetrofitClient by KoinJavaComponent.inject(RetrofitClient::class.java)
    private val itemDao: ItemDao = ItemsDatabase.getInstance(RssReader.application)!!.itemDao()
    private var articlesLocalData = ArrayList<Article>()

    suspend fun updateArticle(article: Article): MutableLiveData<List<Article>> {
        itemDao.updateArticle(article)
        val result : MutableLiveData<List<Article>> = MutableLiveData(listOf())
        val index = findElementInList(article, articlesLocalData)
        articlesLocalData[index].setFavorite(article.isFavorite())
        result.value = articlesLocalData
        return result
    }

    private fun findElementInList(article : Article, list : ArrayList<Article>) : Int{
        list.forEachIndexed { index, element ->
            if (element == article){
                return index
            }
        }
        return -1
    }

    fun fetchRssData(callback : Callback<RSSObject?>){
        retrofitClient.createRSSApi().getRSSObject?.enqueue(callback)
    }

    fun handleRssDataResponse(response : Response<RSSObject?>) : MutableLiveData<List<Article>>{
        val result : MutableLiveData<List<Article>> = MutableLiveData(listOf())
        response.body()!!.RSSItems.let {
            result.value = Converters.convertRssItemsToArticles(it)
        }
        return result
    }

    suspend fun loadArticlesFromDb() : List<Article>{
        val result = itemDao.getArticles()
        articlesLocalData = result as ArrayList
        return result
    }

    suspend fun replaceArticles(articles : List<Article>) : MutableLiveData<List<Article>>{
        itemDao.replaceArticles(articles)
        val result : MutableLiveData<List<Article>> = MutableLiveData(listOf())
        articlesLocalData.clear()
        articlesLocalData.addAll(articles)
        result.value = articlesLocalData
        return result
    }
}