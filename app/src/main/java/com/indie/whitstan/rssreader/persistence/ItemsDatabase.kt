package com.indie.whitstan.rssreader.persistence

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.indie.whitstan.rssreader.model.persistence.Article

@Database(entities = [Article::class], version = 1)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        fun getInstance(context: Context): ItemsDatabase? {
            if (INSTANCE == null) {
                synchronized(ItemsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                ItemsDatabase::class.java, "rssreader.db")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}