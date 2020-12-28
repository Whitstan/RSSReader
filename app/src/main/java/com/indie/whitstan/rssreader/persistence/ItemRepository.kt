package com.indie.whitstan.rssreader.persistence

import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.koin.java.KoinJavaComponent

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.indie.whitstan.rssreader.RssReader
import com.indie.whitstan.rssreader.model.network.RSSObject
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle
import com.indie.whitstan.rssreader.network.RetrofitClient
import com.indie.whitstan.rssreader.util.Converters
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

interface IItemRepository{
    suspend fun getArticleByGuid(guid : String): Article
    fun insertArticle(article: Article)
    fun insertFavoriteArticle(favoriteArticle: FavoriteArticle)
    fun insertListOfArticles(articles : List<Article>)
    fun updateArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    fun deleteArticles()
    fun deleteFavorite(favorite : FavoriteArticle)
}

class ItemRepository: IItemRepository {
    private val retrofitClient : RetrofitClient by KoinJavaComponent.inject(RetrofitClient::class.java)
    private val itemDao: ItemDao = ItemsDatabase.getInstance(RssReader.application)!!.itemDao()

    override suspend fun getArticleByGuid(guid : String): Article {
        return itemDao.getArticleByGuid(guid)
    }

    override fun insertArticle(article: Article) {
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.insertArticle(article)
        }
    }

    override fun insertFavoriteArticle(favoriteArticle: FavoriteArticle) {
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.insertFavoriteArticle(favoriteArticle)
        }
    }

    override fun insertListOfArticles(articles : List<Article>){
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.insertListOfArticles(articles)
        }
    }

    override fun updateArticle(article: Article) {
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.updateArticle(article)
        }
    }

    override suspend fun deleteArticle(article: Article) {
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.deleteArticle(article)
        }
    }

    override fun deleteArticles(){
        GlobalScope.launch(Dispatchers.IO) {
            itemDao.deleteArticles()
        }
    }

    override fun deleteFavorite(favorite : FavoriteArticle){
        GlobalScope.launch(Dispatchers.IO) {
            val articleToUpdate = itemDao.getArticleByGuid(favorite.guid)
            if (articleToUpdate != null) {
                articleToUpdate.setFavorite(!articleToUpdate.isFavorite())
                itemDao.updateArticle(articleToUpdate)
            }
            itemDao.deleteFavoriteArticle(favorite.guid)
        }
    }

    fun fetchRssData(itemViewModel : ItemViewModel) {
        retrofitClient.createRSSApi().getRSSObject?.enqueue(object : Callback<RSSObject?> {
            override fun onResponse(call: Call<RSSObject?>, response: Response<RSSObject?>) {
                if (response.body() != null){
                    val rssItems = response.body()!!.RSSItems
                    if (rssItems != null){
                        val articles = Converters.convertRssItemsToArticles(rssItems)
                        deleteArticles()
                        insertListOfArticles(articles)
                        itemViewModel.articlesMediatorData.postValue(articles)
                    }
                }
            }
            override fun onFailure(call: Call<RSSObject?>, error: Throwable) {
                error.printStackTrace()
            }
        })
    }

    fun loadArticlesFromDb(itemViewModel: ItemViewModel){
        itemViewModel.viewModelScope.launch {
            itemViewModel.articlesMediatorData.postValue(itemDao.getArticles())
        }
    }

    fun loadFavoritesFromDb(itemViewModel : ItemViewModel){
        itemViewModel.viewModelScope.launch {
            itemViewModel.favoritesMediatorData.postValue(itemDao.getFavorites())
        }
    }
}