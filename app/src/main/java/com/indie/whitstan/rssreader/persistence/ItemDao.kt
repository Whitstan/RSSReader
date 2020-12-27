package com.indie.whitstan.rssreader.persistence

import androidx.room.*

import com.indie.whitstan.rssreader.model.persistence.Article

@Dao
interface ItemDao {
    @Query("SELECT * FROM articles")
    suspend fun getArticles(): List<Article>

    @Query("SELECT * FROM articles WHERE guid=:guidParam ")
    suspend fun getArticleByGuid(guidParam: String): Article

    @Query("SELECT * FROM articles WHERE favorite = 1")
    suspend fun getFavorites(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfArticles(articles : List<Article>)

    @Query("SELECT 1 FROM articles WHERE favorite = 1 AND guid=:guidParam")
    suspend fun isArticleFavoriteAlready(guidParam: String): Boolean

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles WHERE favorite = 0")
    suspend fun deleteArticlesExceptFavorites()
}