package com.indie.whitstan.rssreader.persistence

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.indie.whitstan.rssreader.model.Item

@Database(entities = [Item::class], version = 1)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun rssItemDao(): ItemDao

    companion object {

        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        fun getInstance(context: Context): ItemsDatabase? {
            if (INSTANCE == null) {
                synchronized(ItemsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                ItemsDatabase::class.java, "RSSItems.db")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}