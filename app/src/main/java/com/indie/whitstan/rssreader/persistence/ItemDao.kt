package com.indie.whitstan.rssreader.persistence

import androidx.room.*

import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle

@Dao
interface ItemDao {
    @Query("SELECT * FROM articles")
    suspend fun getArticles(): List<Article>

    @Query("SELECT * FROM articles WHERE id=:idParam")
    suspend fun getArticleByGuid(idParam: Long): Article

    @Query("SELECT * FROM favoritearticles")
    suspend fun getFavorites(): List<FavoriteArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArticle(favoriteArticle: FavoriteArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfArticles(articles : List<Article>)

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles")
    suspend fun deleteArticles()

    @Transaction
    suspend fun replaceArticles(articles : List<Article>){
        deleteArticles()
        insertListOfArticles(articles)
    }

    @Query("DELETE FROM favoritearticles WHERE id=:idParam")
    suspend fun deleteFavoriteArticle(idParam : Long)
}