package com.indie.whitstan.rssreader.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

import com.indie.whitstan.rssreader.model.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(item: Item?)

    @get:Query("SELECT * FROM items")
    val getAllItems: LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE guid=:guidParam ")
    fun getSingleRSSItemById(guidParam: String): LiveData<Item>

    @Query("SELECT EXISTS (SELECT 1 FROM items WHERE guid =:guidParam)")
    suspend fun isRSSItemStoredInDB(guidParam: String): Boolean

    @Update
    fun updateItem(item: Item)

    @Delete
    fun deleteItem(item: Item?)

}