package com.indie.whitstan.rssreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import com.indie.whitstan.rssreader.model.Item
import com.indie.whitstan.rssreader.persistence.ItemDao
import com.indie.whitstan.rssreader.persistence.ItemsDatabase

class ItemViewModel(application: Application) : AndroidViewModel(application){
    private val itemDao: ItemDao
    internal val allItems: LiveData<List<Item>>

    init {
        val rssItemDB = ItemsDatabase.getInstance(application)
        itemDao = rssItemDB!!.rssItemDao()
        allItems = itemDao.getAllItems
    }

    fun insertRSSItem(item: Item?) {
        GlobalScope.launch {
            if (item != null){ // TODO: not the best solution for ensuring null values are not passed...
                itemDao.insertItem(item)
            }
        }
    }

    fun getSingleRSSItemById(guid : String): LiveData<Item> {
        return itemDao.getSingleRSSItemById(guid)
    }

    suspend fun isRSSItemStoredInDB(guid : String?): Boolean{
        if (guid != null){ // TODO: not the best solution for ensuring null values are not passed...
            return itemDao.isRSSItemStoredInDB(guid)
        }
        else{
            return false
        }
    }

    fun updateRSSItem(item: Item?) {
        GlobalScope.launch {
            if (item != null){ // TODO: not the best solution for ensuring null values are not passed...
                itemDao.updateItem(item)
            }
        }
    }

    fun deleteRSSItem(item: Item?) {
        GlobalScope.launch {
            if (item != null){ // TODO: not the best solution for ensuring null values are not passed...
                itemDao.deleteItem(item)
            }
        }
    }
}