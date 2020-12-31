package com.indie.whitstan.rssreader.persistence

import androidx.room.*

import com.indie.whitstan.rssreader.model.persistence.Article

@Dao
interface ItemDao {
    @Query("SELECT * FROM articles")
    suspend fun getArticles(): List<Article>

    @Query("SELECT * FROM articles WHERE id=:idParam")
    suspend fun getArticleByGuid(idParam: Long): Article

    @Query("SELECT * FROM articles WHERE favorite = 1")
    suspend fun getFavorites(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfArticles(articles : List<Article>)

    @Transaction
    suspend fun replaceArticles(articles : List<Article>){
        deleteArticles()
        insertListOfArticles(articles)
    }

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles")
    suspend fun deleteArticles()
}