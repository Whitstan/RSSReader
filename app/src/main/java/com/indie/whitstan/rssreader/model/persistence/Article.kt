package com.indie.whitstan.rssreader.model.persistence

import java.util.*

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "articles")
class Article : BaseObservable() {
    @field:PrimaryKey
    @field:ColumnInfo(name = "guid")
    var guid: String = UUID.randomUUID().toString()

    @field:ColumnInfo(name = "title")
    var title: String? = null

    @field:ColumnInfo(name = "link")
    var link: String? = null

    @field:ColumnInfo(name = "pubDate")
    var pubDate: String? = null

    @field:ColumnInfo(name = "description")
    var description: String? = null

    @field:ColumnInfo(name = "favorite")
    var fav: Boolean = false

    @Bindable
    fun isFavorite() : Boolean{
        return fav
    }

    @JvmName("setFavorite_")
    fun setFavorite(newValue : Boolean){
        fav = newValue
        notifyPropertyChanged(BR.favorite);
    }
}