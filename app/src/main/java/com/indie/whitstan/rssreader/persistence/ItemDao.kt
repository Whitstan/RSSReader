package com.indie.whitstan.rssreader.persistence

import androidx.room.*
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle

import com.indie.whitstan.rssreader.model.persistence.LocalArticle

@Dao
interface ItemDao {
    @Query("SELECT * FROM articles")
    suspend fun getArticles(): List<LocalArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfArticles(localArticles : List<LocalArticle>)

    @Transaction
    suspend fun replaceArticles(localArticles : List<LocalArticle>){
        deleteArticles()
        insertListOfArticles(localArticles)
    }

    @Update
    suspend fun updateArticle(localArticle: LocalArticle)

    @Query("DELETE FROM articles")
    suspend fun deleteArticles()

    // Favorites

    @Query("SELECT * FROM favorites")
    suspend fun getFavoriteArticles(): List<FavoriteArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArticle(favoriteArticle: FavoriteArticle)

    @Delete
    suspend fun deleteFavoriteArticle(favoriteArticle: FavoriteArticle)
}